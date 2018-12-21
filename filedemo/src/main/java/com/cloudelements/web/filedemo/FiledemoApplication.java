package com.cloudelements.web.filedemo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class FiledemoApplication {
	
	private static final Contact CONTACT = new Contact("Satish Vatada", "localhost:8080", "vatada.satish@gmail.com");
	private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("FileDemo Application",
			"Filesystem rest API to search, download and upload files.", "1.0", "urn:tos", CONTACT, "License-2.0",
			"urn:tos");
	private static final Set<String> DEFAULT_PRODUCES = new HashSet<String>(Arrays.asList("application/json"));

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO).produces(DEFAULT_PRODUCES).select()
				.paths(paths())
				.build();
	}

	private Predicate<String> paths() {
		// Exclude Spring error controllers
		return Predicates.not(PathSelectors.regex("/errors"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FiledemoApplication.class, args);
	}
}

