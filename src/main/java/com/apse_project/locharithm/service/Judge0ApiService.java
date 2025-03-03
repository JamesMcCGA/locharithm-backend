package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dtos.SubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String JUDGE0_POST_ENDPOINT =
            "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=true&wait=false&fields=*";



    public ResponseEntity<String> submitCode(SubmissionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", "judge0-ce.p.rapidapi.com");
        headers.set("x-rapidapi-key", "dc587250bamshc1dbc6841eb9fc9p1ed2b5jsn423dd8dbefe8");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> result = restTemplate.exchange(JUDGE0_POST_ENDPOINT,
                HttpMethod.POST,
                entity,
                String.class);
        return result;
    }

    public String getLanguages() {
        String languagesEndpoint = "https://judge0-ce.p.rapidapi.com/languages";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", "judge0-ce.p.rapidapi.com");
        headers.set("x-rapidapi-key", "dc587250bamshc1dbc6841eb9fc9p1ed2b5jsn423dd8dbefe8");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                languagesEndpoint,
                HttpMethod.GET,
                entity,
                String.class);
        return result.getBody();
    }



    public SubmissionRequest createHttpSubmissionRequestFromCode(String plainCode   ){
        SubmissionRequest request = new SubmissionRequest();
        request.setLanguageId(52);

        String encodedCode = Base64.getEncoder()
                .encodeToString(plainCode.getBytes(StandardCharsets.UTF_8));
        request.setSourceCode(encodedCode);

        request.setStdin("SnVkZ2Uw");
        return request;
    }

}
