package com.asp.eiyu.ldap.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);

	@Value("${asp.login.defualt.user}")
	private  String  defaultUser;

	@Value("${asp.login.defualt.pwd}")
	private  String password;


	/* 
	if ("javainuse".equals(username)) {
		return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
				new ArrayList<>());
	} else {
		throw new UsernameNotFoundException("User not found with username: " + username);
	} */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		LOGGER.info(" * LOAD jwtUserDetailsService Validation * User: {} ",username);
		
		 if(this.defaultUser.equals(username)){
			return new User(username, this.password, 
			AuthorityUtils.createAuthorityList("ROLE_USER"));
		} else {
			throw new UsernameNotFoundException("User not found with username: " .concat(username) );
		}

	}
	
}