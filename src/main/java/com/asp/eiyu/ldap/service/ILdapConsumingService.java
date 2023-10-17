package com.asp.eiyu.ldap.service;

import com.asp.eiyu.ldap.dto.LoginLdapRequest;
import com.asp.eiyu.ldap.dto.LoginLdapResponse;

public interface ILdapConsumingService {
    
    LoginLdapResponse processLdapValidation(LoginLdapRequest ldapRequest);

}
