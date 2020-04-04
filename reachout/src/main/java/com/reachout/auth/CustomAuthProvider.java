package com.reachout.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.User;

public class CustomAuthProvider implements AuthenticationProvider {
	public final Logger logger = LogManager.getLogger(CustomAuthProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		User foundUser = searchForValidUser(username, password);
		if (foundUser != null) {
			SystemUser userForContext = new SystemUser(foundUser);
			// use the credentials
			// and authenticate against the third-party system
			return new UsernamePasswordAuthenticationToken(userForContext, username, userForContext.getAuthorities());
		} else {
			return null;
		}
	}

	private User searchForValidUser(String username, String password) {
		if (username == null || password == null || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			logger.debug("Login denied for user: " + username);
			return null;
		}

		// Check to see if the user is valid
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		User userFound = userDAO.selectUser(username);

		// Either we found nothing or the password wasn't valid
		if (userFound == null || !userFound.getPassword().equals(password)) {
			logger.debug("Invalid password provided for user : " + username);
			return null;
		}

		// If we reached here then we found a valid user
		return userFound;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
