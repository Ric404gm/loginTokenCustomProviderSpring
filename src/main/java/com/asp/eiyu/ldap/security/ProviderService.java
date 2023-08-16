package com.asp.eiyu.ldap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class ProviderService   implements AuthenticationProvider{


    @Autowired
    @Qualifier("jwtUserDetailsService")
    private  UserDetailsService userDetailsService;


    /*
     * Add your service validation  if  is ok  ? return populated UsernamePasswordAuthenticationToken  else can be return null for unauthorized
     */

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        

        Object username = authentication.getPrincipal();
        Object password = authentication.getCredentials();
        
         UsernamePasswordAuthenticationToken user = 
            new  UsernamePasswordAuthenticationToken(username, password,
                 AuthorityUtils.createAuthorityList("ROLE_USER")); 
        //userDetailsService.loadUserByUsername(null)
        //user.setDetails(authentication.getDetails());
        return user;
    }

     @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
