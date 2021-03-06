package com.webproject.pms.config;

import com.webproject.pms.util.AuthProvider;
import com.webproject.pms.util.OAuth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthProvider authProvider;
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(8);
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
						"/oauth2/**",
						"/login/**",
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
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authProvider);
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
