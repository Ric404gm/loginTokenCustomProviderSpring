package com.asp.eiyu.ldap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;



public class CustomManager implements AuthenticationManager  {

    
	private static final Logger LOGGER = LoggerFactory.getLogger("AUTHTRACE");


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info(" *  CustomManager implements AuthenticationManager .... ");

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
             authentication.getCredentials(),AuthorityUtils.createAuthorityList("ROLE_USER"));
        
    }   
    
}
