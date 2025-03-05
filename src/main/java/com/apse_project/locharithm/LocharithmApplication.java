	package com.apse_project.locharithm;
	
	import com.apse_project.locharithm.dtos.SubmissionRequest;
	import com.apse_project.locharithm.service.Judge0ApiService;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.context.ApplicationContext;
	import org.springframework.http.ResponseEntity;

	@SpringBootApplication
	public class LocharithmApplication {
	
		public static void main(String[] args) {
			ApplicationContext context = SpringApplication.run(LocharithmApplication.class, args);
			Judge0ApiService judge0ApiService = context.getBean(Judge0ApiService.class);
	
			String code = "";
			int languageCode = 0;

			// submitting request and printing response
			ResponseEntity<String> responseFromApi = judge0ApiService.submitCode(code, languageCode);
			System.out.println(responseFromApi.getBody());
		}
	}
	
