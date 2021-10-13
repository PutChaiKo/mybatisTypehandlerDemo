package com.putchaiko.mybatisTypehandlerDemo;

import com.putchaiko.mybatisTypehandlerDemo.dog.DogMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@MapperScan("com.putchaiko.mybatisTypehandlerDemo")
public class MybatisTypehandlerDemoApplication {

	@Resource
	private DogMapper dogMapper;

	public static void main(String[] args) {
		SpringApplication.run(MybatisTypehandlerDemoApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return dogMapper.selectDogName(1);
	}


}
