	package com.apse_project.locharithm;
	
	import com.apse_project.locharithm.dtos.SubmissionRequest;
	import com.apse_project.locharithm.service.Judge0ApiService;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
	import org.springframework.context.ApplicationContext;
	import org.springframework.http.ResponseEntity;

	@SpringBootApplication
	@EnableAutoConfiguration(exclude = {
			SecurityAutoConfiguration.class
	})
	public class LocharithmApplication {
	
		public static void main(String[] args) {
			ApplicationContext context = SpringApplication.run(LocharithmApplication.class, args);
		}
	}
	
