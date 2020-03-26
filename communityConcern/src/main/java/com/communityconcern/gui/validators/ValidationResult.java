package com.communityconcern.gui.validators;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {

	private boolean outcome = true;
	private Map<String, String> errors;

	public ValidationResult() {
		errors = new HashMap<>();
	}

	public void setOutcome(boolean outcome) {
		this.outcome = outcome;
	}

	public boolean getOutcome() {
		return outcome;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void addError(String key, String value) {
		errors.put(key, value);
	}

}
