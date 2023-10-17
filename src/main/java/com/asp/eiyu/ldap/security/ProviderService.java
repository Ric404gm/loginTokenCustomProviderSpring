package com.asp.eiyu.ldap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asp.eiyu.ldap.dto.LoginLdapRequest;
import com.asp.eiyu.ldap.dto.LoginLdapResponse;
import com.asp.eiyu.ldap.service.ILdapConsumingService;


@Service
public class ProviderService implements AuthenticationProvider{

    private static final Logger LOGGER = LoggerFactory.getLogger("AUTHTRACE");
    private static final String LDAP_OK_LOGIN_CODE = "0"; 


    private final  ILdapConsumingService ldapConsumingService;

    @Autowired
    public ProviderService (ILdapConsumingService ldapConsumingService) {
        this.ldapConsumingService = ldapConsumingService;
    }



    /*
     * Add your service validation  if  is ok  ? return populated UsernamePasswordAuthenticationToken  else can be return null for unauthorized
     * 
     *  Object username = authentication.getPrincipal();
        Object password = authentication.getCredentials();
        
         UsernamePasswordAuthenticationToken user = 
            new  UsernamePasswordAuthenticationToken(username, password,
                 AuthorityUtils.createAuthorityList("ROLE_USER")); 
        //userDetailsService.loadUserByUsername(null)
        //user.setDetails(authentication.getDetails());
        return user;

         **** Only for Tests ***
        LoginLdapResponse ldapResponse =  ldapConsumingService.processLdapValidation(ldapRequest);       
        ldapResponse = new LoginLdapResponse(); 
        ldapResponse.setNumempleado("404");
        ldapResponse.setIdUsuario("5347");
        ldapResponse.setCodeEstatus("0");
        ldapResponse.setMessageEstatus("msgstatus");
        ldapResponse.setMensaje("MensajeExtra");
        ldapResponse.setData("dataextra"); 

     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
        LOGGER.info(" Step ProviderService Authentication authenticate....");
        
        Object username = authentication.getPrincipal();
        Object password = authentication.getCredentials();

        LoginLdapRequest ldapRequest = new LoginLdapRequest();
        ldapRequest.setUsuario("AMCARRIZOZA");
        ldapRequest.setContrasena("b7da45996a47a61031fd341c3d898c41b600609d47de2f32a297580fe1612002");
        ldapRequest.setIdUsuario("5347");
        ldapRequest.setIdSesion("0");
        ldapRequest.setEstatus(1);
        ldapRequest.setIdAplicativo(7);

        
        LoginLdapResponse ldapResponse =  ldapConsumingService.processLdapValidation(ldapRequest);       
     
        if( null == ldapResponse){
            throw new UsernameNotFoundException(" Ha ocurrido un error en la autenticacion Ldap");
        }
        if(!LDAP_OK_LOGIN_CODE.equals(ldapResponse.getCodeEstatus()))
        { 
            throw new UsernameNotFoundException(" Ha ocurrido un error en la autenticacion Ldap");
        }
        return  new  UsernamePasswordAuthenticationToken(username, password,
                    AuthorityUtils.createAuthorityList("ROLE_USER"));

    }

     @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
