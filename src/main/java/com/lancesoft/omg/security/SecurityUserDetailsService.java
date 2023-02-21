package com.lancesoft.omg.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.UserRegistrationDao;
import com.lancesoft.omg.entity.Authorities;
import com.lancesoft.omg.entity.UserRegistrationEntity;
@Service("securityUserDetailService")
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRegistrationDao userRegistrationDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserRegistrationEntity userRegistrationEntity=userRegistrationDao.findByUserName(username);
		System.out.println("Inside load username >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<Authorities> ud=userRegistrationEntity.getAuthorities();
		System.out.println(ud);
		SecurityUserdetails securityUserDetails=null;
		if(userRegistrationEntity!= null)
		{
			securityUserDetails=new SecurityUserdetails();
			securityUserDetails.setUserRegistrationEntity(userRegistrationEntity);
		
		}
		else
		{
			throw new UsernameNotFoundException("user not exist with name :"+username);
			
		}
		return securityUserDetails;
	}

}
