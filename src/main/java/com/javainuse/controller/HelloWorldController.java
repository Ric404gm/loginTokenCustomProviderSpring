package com.javainuse.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class HelloWorldController {

	@GetMapping({ "/hello" })
	@ResponseBody
	public String hello() {
		return "Hello World";
	}

}
