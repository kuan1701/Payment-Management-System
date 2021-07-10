package com.webproject.pms.config;

import com.webproject.pms.service.impl.UserServiceImpl;
import com.webproject.pms.util.AuthProvider;
import com.webproject.pms.util.OAuth2.CustomOAuth2UserService;
import com.webproject.pms.util.OAuth2.CustomUserInfoTokenServices;
import com.webproject.pms.util.OAuth2.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private AuthProvider authProvider;
	
	@Autowired
	@Qualifier("oauth2ClientContext")
	private OAuth2ClientContext oAuth2ClientContext;
	@Autowired
	private CustomUserInfoTokenServices customUserInfoTokenServices;
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		
		return new BCryptPasswordEncoder(8);
	}
	
	@Bean
	public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(oAuth2ClientContextFilter);
		registration.setOrder(-100);
		return registration;
	}
	
	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login/google");
		
		OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
		googleFilter.setRestTemplate(googleTemplate);
		customUserInfoTokenServices.customUserInfoTokenServices(googleResource().getUserInfoUri(),
				google().getClientId());
		customUserInfoTokenServices.setRestTemplate(googleTemplate);
		googleFilter.setTokenServices(customUserInfoTokenServices);
		
		return googleFilter;
	}
	
	@Bean
	@ConfigurationProperties("google.client")
	public AuthorizationCodeResourceDetails google() {
		return new AuthorizationCodeResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("google.resource")
	public ResourceServerProperties googleResource() {
		return new ResourceServerProperties();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				//Доступ только для не зарегистрированных пользователей
				.antMatchers(
						"/forgot-password",
						"/registration",
						"/registration-message",
						"/activate/*"
				).not().fullyAuthenticated()
				//Доступ только для пользователей с ролью Администратор
				.antMatchers("/admin/**").hasRole("ADMIN")
				//Доступ только для пользователей с ролью Пользователь
				.antMatchers("/my-account").hasRole("USER")
				//Доступ разрешен всем пользователям
				.antMatchers(
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
				.userInfoEndpoint()
				.userService(oAuth2UserService)
				.and()
				.successHandler(oAuth2LoginSuccessHandler)
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
