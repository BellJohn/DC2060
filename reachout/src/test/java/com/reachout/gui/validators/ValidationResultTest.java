package com.reachout.gui.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ValidationResultTest {

	/**
	 * Tests whether we can submit data which is missing entries 
	 */
	@Test
	public void missingDataTest() {
		Map<String, String> invalidData = new HashMap<>();
		ValidationResult result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("Wrong number of entries provided"));

		result = SignupValidator.validateSignupForm(null);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("No data supplied"));
	}

	/**
	 * Tests for poorly formed data with whitespaces and null entries
	 */
	@Test
	public void dataWithWhitespaceTest() {
		Map<String, String> invalidData = new HashMap<>();
		invalidData.put("firstName", "first");
		invalidData.put("lastName", "last");
		invalidData.put("user", null);
		invalidData.put("email", "goodemail@test.com");
		invalidData.put("dob", "20/09/1993");
		invalidData.put("password", "bad password with spaces");
		invalidData.put("passwordConfirm", "bad password with spaces");
		
		ValidationResult result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("Value contained whitespace"));
		assertTrue(result.getErrors().containsKey("Empty value for key"));

		invalidData.remove("user");
		invalidData.put("user key", "testValue");
		result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("Key contained whitespace"));
		
		invalidData.remove("user key");
		invalidData.put(null, "testValue");
		
		result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("Empty key"));
		
		
	}

	/**
	 * Tests to see if the regex can detect (in)valid emails
	 */
	@Test
	public void EmailSyntaxTest() {
		Map<String, String> invalidData = new HashMap<>();
		invalidData.put("firstName", "first");
		invalidData.put("lastName", "last");
		invalidData.put("email", "notmatchingRegex.COM");
		invalidData.put("user", "user");
		invalidData.put("dob", "20/09/1993");
		invalidData.put("password", "password");
		invalidData.put("passwordConfirm", "password");

		ValidationResult result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertEquals("notmatchingRegex.COM", result.getErrors().get("Email is not of valid form"));

		// Copy the invalid data but replace the email with a good one
		Map<String, String> validData = new HashMap<>();
		validData.putAll(invalidData);
		validData.put("email", "good.email@test.com");
		result = SignupValidator.validateSignupForm(validData);
		assertTrue(result.getOutcome());
		assertTrue(result.getErrors().isEmpty());
	}
	
	@Test
	public void nonMatchingPasswordsTest() {
		Map<String, String> invalidData = new HashMap<>();
		invalidData.put("firstName", "first");
		invalidData.put("lastName", "last");
		invalidData.put("email", "goodemail@test.com");
		invalidData.put("user", "user");
		invalidData.put("dob", "20/09/1993");
		invalidData.put("password", "password");
		invalidData.put("passwordConfirm", "passwordOTHER");

		ValidationResult result = SignupValidator.validateSignupForm(invalidData);
		assertFalse(result.getOutcome());
		assertTrue(result.getErrors().containsKey("Passwords do not match"));
	}
}
