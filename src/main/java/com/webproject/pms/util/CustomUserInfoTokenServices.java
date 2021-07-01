package com.webproject.pms.util;

import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.MailSender;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {
    
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    
    protected final Log logger = LogFactory.getLog(this.getClass());
    private String userInfoEndpointUrl;
    private String clientId;
    private OAuth2RestOperations restTemplate;
    
    
    public void customUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {

        Map map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("error")) {
            System.out.println("userInfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
    }

    @Transactional
    public OAuth2Authentication extractAuthentication(Map<String, Object> map) {

        if (userDao.findUserByEmail(String.valueOf(map.get("email"))) == null) {
            User user = new User();
            user.setName(String.valueOf(map.get("given_name")));
            user.setSurname(String.valueOf(map.get("family_name")));
            user.setPassword(passwordEncoder.encode((CharSequence) map.get("sub")));
            user.setEmail(String.valueOf(map.get("email")));
            user.setRole(new Role(1L, "ROLE_USER"));
    
            userDao.save(user);
            if (user.getEmail() != null) {
                if (!StringUtils.isEmpty(user.getEmail())) {
                    String message = String.format(
                            "Hello,%s! \nWelcome to the our Payment Management Service!\nYour Username: %s\nYour Password: %s",
                            user.getName(), user.getUsername(), map.get("sub")
                    );
                    mailSender.send(user.getEmail(), "Welcome!", message);
                }
            }
    
            OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null, null, null, null, null);
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                    "N/A", Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName())));
            token.setDetails(map);
            return new OAuth2Authentication(request, token);
        } else {
            User user = userDao.findUserByEmail(String.valueOf(map.get("email")));
    
            OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null, null, null, null, null);
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                    "N/A", Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName())));
            token.setDetails(map);
    
            return new OAuth2Authentication(request, token);
        }
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map getMap(String path, String accessToken) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Getting user info from: " + path);
        }
        
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }

            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                String tokenType = "Bearer";
                token.setTokenType(tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }

            return restTemplate.getForEntity(path, Map.class, new Object[0]).getBody();
        } catch (Exception var6) {
            this.logger.warn("Could not fetch user details: " + var6.getClass() + ", " + var6.getMessage());
            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }
}



