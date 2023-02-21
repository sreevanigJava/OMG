package com.lancesoft.omg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity(debug = true)
public class MySecurityConfig extends  WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtFilter jwtFilter;
public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception
{
	managerBuilder.userDetailsService(userDetailsService).passwordEncoder(getPassWordEncoder());
	
}
@Override
protected void configure(HttpSecurity httpSecurity) throws Exception
{
	httpSecurity.cors();
	httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Authorization"));
	httpSecurity.csrf().disable();
	httpSecurity.authorizeRequests().antMatchers("/api/user/sendotp","/api/userRegister","/api/login","/api/user/myProfile","/api/admin/sendOtp","/api/admin/register","/api/adminProfile","/api/updateAdmin","/api/updateUser","/api/changePassword","/api/user/changePassword","/api/admin/addCategories","/api/admin/addProducts","/api/admin/addInventory","/api/admin/getAllCategories","/api/admin/getAllProducts","/api/admin/getAllinventory","/api/admin/getOneCategory","/api/user/addToCart","/api/user/getMyCart","/api/user/getAllCartList","/api/user/deleteCart","/api/user/deleteCartList","/api/user/updateCart","/api/user/addAddress","/api/user/placeOrderWithCOD","/api/user/generatePdf").permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

}
@Bean
public PasswordEncoder getPassWordEncoder()
{
	return new BCryptPasswordEncoder();

}
@Bean(name = BeanIds.AUTHENTICATION_MANAGER)

public AuthenticationManager authenticationManagerBean() throws Exception
{
return super.authenticationManager();

}
}