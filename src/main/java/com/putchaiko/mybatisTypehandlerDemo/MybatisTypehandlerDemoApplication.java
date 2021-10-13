package com.putchaiko.mybatisTypehandlerDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MybatisTypehandlerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatisTypehandlerDemoApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}


}
