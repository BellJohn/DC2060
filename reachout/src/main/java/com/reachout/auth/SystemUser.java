package com.reachout.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.reachout.models.User;

public class SystemUser implements UserDetails {

	private static final long serialVersionUID = 4304178124947025820L;
	private final String username;
	private final String password;
	private List<SimpleGrantedAuthority> roles = new ArrayList<>();

	public SystemUser(String username, String password) {
		this.username = username;
		this.password = password;
		SimpleGrantedAuthority SGA = new SimpleGrantedAuthority("USER");
		roles.add(SGA);
	}

	public SystemUser(User foundUser) {
		this(foundUser.getUsername(), foundUser.getPassword());
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
