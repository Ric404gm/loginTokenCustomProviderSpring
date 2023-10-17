package com.asp.eiyu.ldap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

	@GetMapping( "/version" )
	@ResponseBody
	public String  testVersion() throws Exception {
		return "1.0.0";
	}

}
