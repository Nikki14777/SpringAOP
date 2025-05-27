package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	@GetMapping("/test")
	public String testCall() {
		return "Hii!!";
	}
	
	@PostMapping("/postTest")
	public String testPost(@RequestBody DemoRequest data) {
		return data.getName();
	}

}
