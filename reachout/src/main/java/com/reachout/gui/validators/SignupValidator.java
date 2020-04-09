/**
 * 
 */
package com.reachout.gui.validators;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * @author John
 *
 */
public class SignupValidator {

	private static final String EMAIL_PATTERN =  "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	private SignupValidator() {
	}

	/**
	 * Passed a data array containing username, email address, password and password
	 * confirmation. 
	 * </br>Returns a result with the outcome set to true/false (pass/fail) and reasons
	 * for failure
	 * 
	 * @param userData
	 * @return ValidationResult
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

		// If we go false back, more errors have been added which will affect further validation.
		// Return now
		if (!checkForNullEmptyOrWhiteSpace(userData, result)) {
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

		// Ensure passwords match
		if (!userData.get("password").equals(userData.get("passwordConfirm"))) {
			result.setOutcome(false);
			result.addError("Passwords do not match",
					"[" + userData.get("password") + "][" + userData.get("passwordConfirm") + "]");
		}
		return result;
	}

	/**
	 * Checks each element within the entry set provided.
	 * </br>
	 * If a key or value is null, empty or contains whitespace then add to list of errors
	 * @param userDataEntries
	 * @param result
	 * @return true if no errors found. false otherwise
	 */
	private static boolean checkForNullEmptyOrWhiteSpace(Map<String, String> userDataEntries, ValidationResult result) {
		final int numErrorsBefore = result.getErrors().size();

		// Check for null or empty keys or values
		for (Entry<String, String> datum : userDataEntries.entrySet()) {
			// Keys
			if (datum.getKey() == null || StringUtils.isEmpty(datum.getKey())) {
				result.setOutcome(false);
				result.addError("Empty key", datum.getKey());
			} else if (StringUtils.containsWhitespace(datum.getKey())) {
				result.setOutcome(false);
				result.addError("Key contained whitespace", datum.getKey());
			}
			// Values
			if (datum.getValue() == null || StringUtils.isEmpty(datum.getValue())) {
				result.setOutcome(false);
				result.addError("Empty value for key", datum.getKey());
			} else if (StringUtils.containsWhitespace(datum.getValue())) {
				result.setOutcome(false);
				result.addError("Value contained whitespace", datum.getValue());
			}
		}

		return result.getErrors().size() == numErrorsBefore;

	}
}
