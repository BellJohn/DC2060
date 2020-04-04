/**
 * 
 */
package com.reachout.gui.validators;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * @author John
 *
 */
public class SignupValidator {

	private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

	private SignupValidator() {
	}

	/**
	 * Passed a data array containing username, email address, password and password
	 * confirmation. Returns a result with the true/false (pass/fail) and reasons
	 * for failure
	 * 
	 * @param userData
	 * @return
	 */
	public static ValidationResult validateSignupForm(Map<String, String> userData) {
		ValidationResult result = new ValidationResult();
		// Ensure not null
		if (userData == null) {
			result.setOutcome(false);
			result.addError("No data supplied", null);
			return result;
		}
		// Ensure there are as many entries as we expect
		// TODO update to static field in User object
		if (userData.size() != 4) {
			result.setOutcome(false);
			result.addError("Wrong number of entries provided", userData.toString());
			return result;
		}
		// Check for null or empty keys
		boolean badKeyOrValue = false;
		for (String datum : userData.keySet()) {
			if (datum == null || StringUtils.isEmpty(datum)) {
				result.setOutcome(false);
				result.addError("Empty key", datum);
				badKeyOrValue = true;
			} else if (StringUtils.containsWhitespace(datum)) {
				result.setOutcome(false);
				result.addError("Key contained whitespace", datum);
				badKeyOrValue = true;
			}
		}
		if (badKeyOrValue) {
			return result;
		}
		// Check for null or empty values
		for (String datum : userData.keySet()) {
			if (userData.get(datum) == null || StringUtils.isEmpty(userData.get(datum))) {
				result.setOutcome(false);
				result.addError("Empty value for key", datum);
				badKeyOrValue = true;
			} else if (StringUtils.containsWhitespace(datum)) {
				result.setOutcome(false);
				result.addError("Value contained whitespace", datum);
				badKeyOrValue = true;
			}
		}
		if (badKeyOrValue) {
			return result;
		}
		// Check individual components:

		// Does email fit email REGEX?
		if (userData.get("email") != null) {
			String email = userData.get("email");
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			if (!matcher.matches()) {
				result.setOutcome(false);
				result.addError("Email is not of valid form", email);
			}
		}

		//Ensure passwords match
		if (!userData.get("password").equals(userData.get("passwordConfirm"))) {
			result.setOutcome(false);
			result.addError("Passwords do not match", "[" + userData.get("password") + "][" + userData.get("passwordConfirm") + "]");
		}
		return result;
	}
}
