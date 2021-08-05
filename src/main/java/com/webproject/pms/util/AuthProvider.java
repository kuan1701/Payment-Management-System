package com.webproject.pms.util;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthProvider implements AuthenticationProvider {
	
	private final UserServiceImpl userService;
	
	private final PasswordEncoder passwordEncoder;
	
	public AuthProvider(UserServiceImpl userService,
	                    PasswordEncoder passwordEncoder
	) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		User user = (User) userService.loadUserByUsername(username);
		
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new BadCredentialsException("Wrong password");
			}
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			
			return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
