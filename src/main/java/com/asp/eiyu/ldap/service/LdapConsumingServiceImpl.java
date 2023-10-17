package com.asp.eiyu.ldap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.asp.eiyu.ldap.dto.LoginLdapRequest;
import com.asp.eiyu.ldap.dto.LoginLdapResponse;

@Service
public class LdapConsumingServiceImpl implements  ILdapConsumingService {
    

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapConsumingServiceImpl.class);
    private RestTemplate restTemplate;
    private String ldapendpoint; 

    @Autowired
    public LdapConsumingServiceImpl(RestTemplate restTemplate, @Value("${asp.ldap.login}") String ldapendpoint ){
        this.restTemplate = restTemplate;
        this.ldapendpoint = ldapendpoint;
    }     



    /**
     *  HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<LoginLdapResponse>  responseEnt =  restTemplate.postForEntity( this.ldapendpoint, ldapRequest,  LoginLdapResponse.class, headers);
        ......
     */
    @Override
    public LoginLdapResponse processLdapValidation(LoginLdapRequest ldapRequest) {


        try {
            
            LOGGER.info(" * Request asp ldap : {} ", ldapRequest.toString());
            ResponseEntity<LoginLdapResponse>  responseEnt =  restTemplate.postForEntity( this.ldapendpoint, ldapRequest,  LoginLdapResponse.class);
            LOGGER.info(" * Response ldap:   {} ", responseEnt.toString());    
            return responseEnt.getBody();
        }
        catch (Exception exception){
            LOGGER.error("   Ha ocurrido un error en el request ldap: {} ", exception);
            return null;
        }
    }
}
