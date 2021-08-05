package com.webproject.pms.model.oidc;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class CustomOidUser implements OidcUser, Serializable {
	static final long serialVersionUID = 1L;
	
	private final OidcUser oidcUser;
	
	public CustomOidUser(OidcUser oidcUser) {
		this.oidcUser = oidcUser;
	}
	
	@Override
	public Map<String, Object> getClaims() {
		return oidcUser.getClaims();
	}
	
	@Override
	public OidcUserInfo getUserInfo() {
		return oidcUser.getUserInfo();
	}
	
	@Override
	public OidcIdToken getIdToken() {
		return oidcUser.getIdToken();
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return oidcUser.getAttributes();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oidcUser.getAuthorities();
	}
	
	@Override
	public String getName() {
		return oidcUser.getName();
	}
}
