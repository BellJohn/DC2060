/**
 * 
 */
package com.reachout.gui.validators;

import java.util.Arrays;
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
	 * @param data
	 * @return
	 */
	public static ValidationResult validateSignupForm(String[] data) {
		ValidationResult result = new ValidationResult();
		if (data == null) {
			result.setOutcome(false);
			result.addError("No data supplied", null);
			return result;
		}
		if (data.length != 4) {
			result.setOutcome(false);
			result.addError("Wrong number of entries provided", Arrays.deepToString(data));
		}
		for (String datum : data) {
			if (datum == null || StringUtils.isEmpty(datum)) {
				result.setOutcome(false);
				result.addError("Empty value", datum);
			} else if (StringUtils.containsWhitespace(datum)) {
				result.setOutcome(false);
				result.addError("Data contained whitespace", datum);
			}
		}
		if (data[1] != null) {
			System.out.println("email: " + data[1]);
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(data[1]);
			if (!matcher.matches()) {
				result.setOutcome(false);
				result.addError("Email is not of valid form", data[1]);
			}
		}

		if (!data[2].equals(data[3])) {
			result.setOutcome(false);
			result.addError("Passwords do not match", "[" + data[2] + "][" + data[3] + "]");
		}
		return result;
	}
}
