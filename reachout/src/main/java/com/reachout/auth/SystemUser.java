package com.reachout.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.reachout.models.Password;
import com.reachout.models.User;

public class SystemUser implements UserDetails {

	private static final long serialVersionUID = 4304178124947025820L;
	private final String username;
	private final String password;
	private List<SimpleGrantedAuthority> roles = new ArrayList<>();

	public SystemUser(String username, String password) {
		this.username = username;
		this.password = password;
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority("USER");
		roles.add(sga);
	}

	public SystemUser(User foundUser, Password password) {
		this(foundUser.getUsername(), password.getPasswordString());
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
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
