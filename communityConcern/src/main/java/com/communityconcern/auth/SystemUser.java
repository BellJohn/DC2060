package com.communityconcern.auth;

public class SystemUser {

	private final String username;
	private String email;
	private String encPass;

	public SystemUser(String username, String email, String passwordPlain) {
		this.username = username;
		this.email = email;
		this.encPass = passwordPlain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncPass() {
		return encPass;
	}

	public void setEncPass(String encPass) {
		this.encPass = encPass;
	}

	public String getUsername() {
		return username;
	}
}
