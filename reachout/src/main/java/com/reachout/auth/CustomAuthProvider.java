package com.reachout.auth;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		if (isValidUser(username, password)) {

			// use the credentials
			// and authenticate against the third-party system
			return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
		} else {
			return null;
		}
	}

	private boolean isValidUser(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
