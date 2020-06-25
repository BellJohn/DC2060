package com.reachout.auth;

import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Password;
import com.reachout.models.User;

/**
 * ReachOut's custom Spring authentication module. Allows us to map login
 * requests against known entities within our database.
 * 
 * @author John
 *
 */
public class CustomAuthProvider implements AuthenticationProvider {
	public final Logger logger = LogManager.getLogger(CustomAuthProvider.class);

	/**
	 * Authenticate is called by the spring filter chain from the login screen,
	 * passing along the username and password components within the authentication
	 * object. Any invalid credentials will result in a sub type of
	 * AuthenticationException being thrown which can be interpreted on the front
	 * end for appropriate error messages for users to view.
	 * 
	 */
	@Override
	public Authentication authenticate(Authentication authentication) {
		logger.debug("In custom auth filter");
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

		String username = String.valueOf(authToken.getPrincipal());
		String password = String.valueOf(authToken.getCredentials());

		SystemUser foundUser = searchForValidUser(username, password);
		// If the search failed and returned null, push back into spring
		if (foundUser == null) {
			return null;
		}
		// use the credentials
		// and authenticate against the third-party system

		return new UsernamePasswordAuthenticationToken(username, password, foundUser.getAuthorities());
	}

	/**
	 * Internal validation of the username and password combination. Checks to see
	 * if the provided values are not empty/null and then searches the database for
	 * the requested user. If a user is found then the passwords are compared. Any
	 * failure results in a sub type of AuthenticationException being thrown.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws GeneralSecurityException
	 */
	private SystemUser searchForValidUser(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new BadCredentialsException("Missing either username or password, cannot log in");
		}
		logger.debug("Logging in user " + username + "  with pass: " + password);
		// Check to see if the user is valid
		User userFound = null;
		Password passwordFound = null;
		try {
			HibernatePasswordDAOImpl passwordDAO = new HibernatePasswordDAOImpl();
			HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
			userFound = userDAO.selectUser(username);
			if (userFound != null) {
				passwordFound = passwordDAO.selectInUseByUserId(userFound.getId());
			}
		} catch (HibernateException e) {
			logger.error("Unable to close the resource for HibernateUserDAOImpl or HibernatePasswordDAOImpl", e);
			throw e;
		}
		// Either we found nothing or the password wasn't valid
		try {
			if (userFound == null || passwordFound == null || !passwordFound.comparePassword(password)) {
				logger.debug("Login failed. User null? " + (userFound == null) + " passwordFound null? "
						+ (passwordFound == null));
				throw new BadCredentialsException("Unable to login with credentials provided");
			}
		} catch (IllegalStateException | GeneralSecurityException e) {
			logger.error("Something went wrong hashing the password provided", e);
			return null;
		}

		// If we reached here then we found a valid user
		// Convert to SystemUser for the authentication Process
		return new SystemUser(userFound, passwordFound);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
