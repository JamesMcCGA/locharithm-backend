	package com.apse_project.locharithm;
	
	import com.apse_project.locharithm.dtos.SubmissionRequest;
	import com.apse_project.locharithm.service.Judge0ApiService;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.context.ApplicationContext;
	
	@SpringBootApplication
	public class LocharithmApplication {
	
		public static void main(String[] args) {
			ApplicationContext context = SpringApplication.run(LocharithmApplication.class, args);
			Judge0ApiService judge0ApiService = context.getBean(Judge0ApiService.class);
	
			// string of C code
			String code =
					"#include <stdio.h>\n\nint main(void) {\n  char name[10];\n  scanf(\"%s\", name);\n  printf(\"hello, %s\\n\", name);\n  return 0;\n}";
	
			// integer code representing languages, for example Python is 71
			int languageCode = 71;
	
			// creating a http request instance using that string of C code
			SubmissionRequest request = judge0ApiService.createHttpSubmissionRequestFromCode(code, languageCode);
	
			// submitting request and printing response
			String response = String.valueOf(judge0ApiService.submitCode(request));
			System.out.println(response);
		}
	}
	
