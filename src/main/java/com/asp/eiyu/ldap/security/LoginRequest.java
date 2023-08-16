package com.asp.eiyu.ldap.security;

import java.io.Serializable;

import lombok.Data;


@Data
public class LoginRequest implements Serializable {

	
	private String username;
	private String password;
	
	public LoginRequest()	{
		
	}

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password =password;
	}

}