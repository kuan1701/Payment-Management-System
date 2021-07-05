package com.webproject.pms.util.OAuth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;

    public CustomOAuth2User(OAuth2User oauth2User) {
        
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getId() {
        return oauth2User.getAttribute("sub");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String lastName() {
        return oauth2User.getAttribute("family_name");
    }

    public String firstName() {
        return oauth2User.getAttribute("given_name");
    }

    public Boolean emailVerified() {
        return oauth2User.getAttribute("email_verified");
    }

}