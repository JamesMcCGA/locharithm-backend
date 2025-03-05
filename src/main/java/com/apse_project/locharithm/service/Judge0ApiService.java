package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dtos.SubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Service
public class Judge0ApiService {

    @Autowired
    private RestTemplate restTemplate;

    // from spring properties
    @Value("${judge0.api.postEndpoint}")
    private String judge0PostEndpoint;

    @Value("${judge0.api.languagesEndpoint}")
    private String judge0LanguagesEndpoint;

    @Value("${judge0.api.host}")
    private String judge0Host;

    public ResponseEntity<String> submitCode(SubmissionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", judge0Host);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> result = restTemplate.exchange(
                judge0PostEndpoint,
                HttpMethod.POST,
                entity,
                String.class);
        return result;
    }

    public String getLanguages() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", judge0Host);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                judge0LanguagesEndpoint,
                HttpMethod.GET,
                entity,
                String.class);
        return result.getBody();
    }

    public SubmissionRequest createHttpSubmissionRequestFromCode(String plainCode, int languageCode) {
        SubmissionRequest request = new SubmissionRequest();

        String encodedCode = Base64.getEncoder()
                .encodeToString(plainCode.getBytes(StandardCharsets.UTF_8));
        request.setSourceCode(encodedCode);
        request.setLanguageId(languageCode);
        request.setStdin("SnVkZ2Uw");
        return request;
    }
}
