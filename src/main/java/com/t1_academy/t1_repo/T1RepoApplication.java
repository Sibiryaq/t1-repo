package com.t1_academy.t1_repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class T1RepoApplication {
	public static void main(String[] args) {
		SpringApplication.run(T1RepoApplication.class, args);
	}

}
