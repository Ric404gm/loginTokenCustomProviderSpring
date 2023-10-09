package com.asp.eiyu.ldap.security.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResponse implements Serializable {

	private  String token;

	public LoginResponse(String token) {
		this.token = token;
	}

}