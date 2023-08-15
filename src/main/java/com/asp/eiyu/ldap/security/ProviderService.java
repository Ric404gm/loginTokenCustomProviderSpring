package com.asp.eiyu.ldap.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;


@Component
public class ProviderService   implements AuthenticationProvider{
    

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO Auto-generated method stub

        Object username = authentication.getPrincipal();
        Object password = authentication.getCredentials();

          UsernamePasswordAuthenticationToken user = new
           UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.createAuthorityList("ROLE_USER"));

           //  Add your service validation  if  is ok  ? return populated UsernamePasswordAuthenticationToken  
            // else can be return null for unauthorized  
            return user;
        
    }

     @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
