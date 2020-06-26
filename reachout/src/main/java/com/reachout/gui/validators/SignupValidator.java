/**
 * 
 */
package com.reachout.gui.validators;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public class SignupValidator {

	private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	private SignupValidator() {
	}

	/**
	 * Passed a data array containing firstname, lastname, username, date of birth,
	 * email address, password and password confirmation. </br>
	 * Returns a result with the outcome set to true/false (pass/fail) and reasons
	 * for failure
	 * 
	 * @param userData
	 * @return
	 * @return ValidationResult
	 * @throws ParseException
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
		if (userData.size() != 7) {
			result.setOutcome(false);
			result.addError("Wrong number of entries provided", userData.toString());
			return result;
		}

		// If we go false back, more errors have been added which will affect further
		// validation.
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

		// Ensure user is over 18
		if (userData.get("dob") != null) {
			String dobString = userData.get("dob");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dob = LocalDate.parse(dobString, formatter);
			LocalDate currentDate = LocalDate.now();
			int age = Period.between(dob, currentDate).getYears();
			if (age < 18) {
				result.setOutcome(false);
				result.addError("You must be over 18 to sign up", dobString);
			}
		}

		// Ensure passwords match
		if (!userData.get("password").equals(userData.get("passwordConfirm"))) {
			result.setOutcome(false);
			result.addError("Passwords do not match",
					"[" + userData.get("password") + "][" + userData.get("passwordConfirm") + "]");
		}

		checkDuplicateEmailOrUser(userData, result);
		return result;
	}

	private static void checkDuplicateEmailOrUser(Map<String, String> userData, ValidationResult result) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		User userFound = userDAO.selectUser(userData.get("username"));
		if (userFound != null) {
			result.setOutcome(false);
			result.addError("Duplicate Username",
					"The username" + userData.get("username") + " is already taken, please try again.");
		}

		userFound = null;
		userFound = userDAO.getUserByEmail(userData.get("email"));
		if (userFound != null) {
			result.setOutcome(false);
			result.addError("Duplicate Account",
					"An account is already registered with this email address. If you have forgotten your details, try resetting your password");
		}

	}

	/**
	 * Checks each element within the entry set provided. </br>
	 * If a key or value is null, empty or contains whitespace then add to list of
	 * errors
	 * 
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
