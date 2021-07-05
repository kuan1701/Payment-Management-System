package com.webproject.pms.util.OAuth2;

import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = userService.findUserByEmail(oauth2User.getName());
        if (user == null) {
            User newUser = new User(oauth2User.firstName(), oauth2User.lastName(), oauth2User.getEmail(),
                    oauth2User.emailVerified());
            newUser.setRole(new Role(1L, "ROLE_USER"));
            userService.saveUser(newUser);
        } else {
            user.setName(oauth2User.firstName());
            user.setSurname(oauth2User.lastName());
            user.setEmail(oauth2User.getEmail());
            userService.saveUser(user);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}