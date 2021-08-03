package com.webproject.pms.config;

import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.util.AuthProvider;
import com.webproject.pms.util.OAuth2.CustomOAuth2UserService;
import com.webproject.pms.util.OAuth2.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Date;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthProvider authProvider;
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
//	@Autowired
//	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		
		return new BCryptPasswordEncoder(8);
	}
	
	@Bean
	public PrincipalExtractor principalExtractor(UserDao userDao) {
		return map -> {
			Long id = (Long) map.get("sub");

			User user = userDao.findById(id).orElseGet(() -> {
				User newUser = new User();

				newUser.setUserId(id);
				newUser.setName((String) map.get("given_name"));
				newUser.setSurname((String) map.get("family_name"));
				newUser.setEmail((String) map.get("email"));
				newUser.setEmailVerified((Boolean) map.get("email_verified"));
				newUser.setUsername(null);
				newUser.setPhone("+375291111111");
				newUser.setRegistrationDate(new Date().toString());
				newUser.setRole(new Role(1L, "ROLE_USER"));
				newUser.setActivationCode(null);
				newUser.setActive(true);

				return newUser;
			});
			return userDao.save(user);
		};
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.authorizeRequests()
				//Доступ только для не зарегистрированных пользователей
				.antMatchers(
						"/forgot-password",
						"/reset_password",
						"/registration",
						"/registration-message",
						"/verify",
						"/oauth2/**"
				).not().fullyAuthenticated()
				//Доступ только для пользователей с ролью Администратор
				.antMatchers("/my-account").hasRole("ADMIN")
				//Доступ только для пользователей с ролью Пользователь
				.antMatchers("/my-account").hasRole("USER")
				//Доступ разрешен всем пользователям
				.antMatchers(
						"/my-account",
						"/login",
						"/user/**",
						"/resources/**",
						"/bootstrap/**",
						"/images/**",
						"/css/**",
						"/js/**").permitAll()
				.anyRequest().authenticated()
				.and()
				//Настройка для входа в систему
				.formLogin()
				.loginPage("/login")
				//Перенарпавление на страницу после успешного входа
				.defaultSuccessUrl("/my-account", true)
				.permitAll()
				.and()
				.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/my-account", true)
				.userInfoEndpoint()
				.userService(oAuth2UserService)
				.and()
//				.successHandler(oAuth2LoginSuccessHandler)
				.and()
				.logout()
				.permitAll()
				.logoutSuccessUrl("/login");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authProvider);
		
//		auth.userDetailsService(userServiceImpl)
//				.passwordEncoder(passwordEncoder);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/resources/**",
				"/bootstrap/**",
				"/images/**",
				"/css/**",
				"/js/**");
	}
}
