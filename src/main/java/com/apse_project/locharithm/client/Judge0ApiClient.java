package com.apse_project.locharithm.client;

import com.apse_project.locharithm.dtos.SubmissionRequest;
import com.apse_project.locharithm.responses.Judge0ResponseParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Component
public class Judge0ApiClient {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Judge0ResponseParser judge0ResponseParser;

    @Value("${judge0.api.postEndpoint}")
    private String judge0PostEndpoint;

    @Value("${judge0.api.host}")
    private String judge0Host;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final int POLLING_INTERVAL_IN_MILLIS = 1000;

    private static final int MAXIMUM_POLLING_ATTEMPTS = 5;

    /**
     * Creates the HTTP headers to be used in the POST submission.
     */
    public HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("host", judge0Host);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * Creates the HTTP request instance to be sent to the POST submission endpoint.
     */
    public SubmissionRequest createHttpSubmissionRequestFromCode(String plainCode, String stdin, String expectedOutput, int languageCode) {
        SubmissionRequest request = new SubmissionRequest();

        String encodedCode = Base64.getEncoder().encodeToString(plainCode.getBytes(StandardCharsets.UTF_8));
        String encodedStdin = Base64.getEncoder().encodeToString(stdin.getBytes(StandardCharsets.UTF_8));
        String encodedExpectedOutput = Base64.getEncoder().encodeToString(expectedOutput.getBytes(StandardCharsets.UTF_8));

        request.setSourceCode(encodedCode);
        request.setLanguageId(languageCode);
        request.setStdin(encodedStdin);
        request.setExpectedOutput(encodedExpectedOutput);
        return request;
    }

    /**
     * Post to the submission endpoint.
     */
    public ResponseEntity<String> postToSubmissionEndpoint(SubmissionRequest submissionRequest, HttpHeaders requestHeaders) {
        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(submissionRequest, requestHeaders);
        return restTemplate.exchange(
                judge0PostEndpoint + "?base64_encoded=true",
                HttpMethod.POST,
                entity,
                String.class);
    }


    /**
     * Gets the result of the submission after posting to the submission endpoint.
     */
    public String getSubmissionResult(String token){
        String url = judge0PostEndpoint + "/" + token + "?base64_encoded=true";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        String finalResponse=null;
        int attempt = 0;

        while (attempt < MAXIMUM_POLLING_ATTEMPTS) {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String temp = judge0ResponseParser.retrieveItemFromJsonBody(response.getBody(), "status.description");
            int statusId = Integer.parseInt(judge0ResponseParser.retrieveItemFromJsonBody(response.getBody(), "status.id"));
            if (statusId != 1 && statusId != 2) {
                finalResponse=temp;
                break;
            }

            attempt++;
            try {
                Thread.sleep(POLLING_INTERVAL_IN_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted during sleep: " + e.getMessage());
                break;
            }
        }
        return finalResponse;
    }
}
