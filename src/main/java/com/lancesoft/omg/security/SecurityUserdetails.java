package com.lancesoft.omg.security;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.entity.UserRegistrationEntity;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Service
public class SecurityUserdetails implements UserDetails{
	
	private UserRegistrationEntity userRegistrationEntity;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userRegistrationEntity.getAuthorities().stream().map(role->new SimpleGrantedAuthority("ROLE"+role)).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return userRegistrationEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userRegistrationEntity.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
