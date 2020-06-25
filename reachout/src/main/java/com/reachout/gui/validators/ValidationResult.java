package com.reachout.gui.validators;

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

	public String prettyPrintErrors() {
		StringBuilder builder = new StringBuilder();
		for (String key : errors.keySet()) {
			builder.append(String.format("<p>%s. %s</p>", key, errors.get(key)));
		}
		return builder.toString();
	}

}
