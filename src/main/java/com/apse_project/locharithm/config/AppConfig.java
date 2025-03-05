package com.apse_project.locharithm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getInterceptors().add((request, body, execution) -> {
//             for debugging, prints out the exact request being sent
            System.out.println("Request URI: " + request.getURI());
            System.out.println("Request Headers: " + request.getHeaders());
            System.out.println("Request Body: " + new String(body, StandardCharsets.UTF_8));
            return execution.execute(request, body);
        });
        return restTemplate;
    }

}
