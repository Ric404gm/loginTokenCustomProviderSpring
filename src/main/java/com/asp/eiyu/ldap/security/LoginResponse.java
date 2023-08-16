package com.asp.eiyu.ldap.security;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResponse implements Serializable {

	private  String jwttoken;

	public LoginResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}