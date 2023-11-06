package com.asp.eiyu.ldap.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.asp.eiyu.ldap.dto.LoginRequest;
import com.asp.eiyu.ldap.dto.LoginResponse;
import com.asp.eiyu.ldap.security.ProviderService;
import com.asp.eiyu.ldap.utils.AesUtil;
import com.asp.eiyu.ldap.utils.JwtTokenUtil;
import com.asp.eiyu.ldap.utils.AesUtil.OperationType;


@RestController
@CrossOrigin("*")
public class JwtAuthenticationController {


	private static final Logger LOGGER = LoggerFactory.getLogger("AUTHTRACE");

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private  AesUtil aesUtil;


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest)
			throws Exception {
		
		LOGGER.info(" Step : Controller /autenticate  ");
		
		Authentication authentication = authenticate(aesUtil.doOperation( authenticationRequest.getUsername() , OperationType.DECRYPT),
			aesUtil.doOperation( authenticationRequest.getPassword() , OperationType.DECRYPT));

		LOGGER.info(" * Result From autentication : {}",authentication.toString() );
		
		Map<String, Object> claims =  new  HashMap<>();
		claims.put("current_token_user", aesUtil.doOperation( authenticationRequest.getUsername() , OperationType.DECRYPT));

		
		final String token = jwtTokenUtil.doGenerateToken(claims, aesUtil.doOperation(
				 authenticationRequest.getUsername() , OperationType.DECRYPT));

		return ResponseEntity.ok(new LoginResponse(token));
	}


	/*
	 * 	Authentication authentication = authenticationManager.authenticate(
	 * 	  providerService.authenticate(  new UsernamePasswordAuthenticationToken(username, password)));
	 */
	private Authentication authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {

			Authentication authenticationProvider = providerService.authenticate(  new UsernamePasswordAuthenticationToken(username, password));
			Authentication authentication = authenticationManager.authenticate( authenticationProvider );
			
			return  authentication;
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
