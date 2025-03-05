package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dtos.SubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Value("${judge0.api.host}")
    private String judge0Host;

    private static final int POLLING_INTERVAL_IN_MILLIS = 500;

    private static final int MAXIMUM_POLLING_ATTEMPTS = 3;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Publicly accessible function to process a code submission end-to-end. Takes a string of plain code, as well as
     * a language code (passed from front-end) and creates a HTTP request to the POST endpoint, then polls the GET endpoint
     * until the submission is done processing.
     *
     * @param plainCode    the pure string of code to be processed
     * @param languageCode the identifier of the programming language used
     * @return the final API response from the GET endpoint
     */
    public ResponseEntity<String> submitCode(String plainCode, int languageCode) {
        SubmissionRequest request = createHttpSubmissionRequestFromCode(plainCode, languageCode);
        HttpHeaders requestHeaders = createHttpHeaders();
        ResponseEntity<String> responseFromSubmissionEndpoint = postToSubmissionEndpoint(request, requestHeaders);

        if (responseFromSubmissionEndpoint.getStatusCode().is2xxSuccessful()) {
            String body = responseFromSubmissionEndpoint.getBody();

            // extract the token using Jackson - not sure if theres a lighter way to do this?
            try {
                JsonNode jsonNode = objectMapper.readTree(body);
                String token = jsonNode.get("token").asText();
                System.out.println("Submission token: " + token);

                // Poll the GET endpoint until the submission is processed
                ResponseEntity<String> finalResponse = getSubmissionResult(token);
                return finalResponse;
            } catch (Exception e) {
                System.out.println("Failed to extract token from JSON: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to submit code: " + responseFromSubmissionEndpoint.getStatusCode());
            System.out.println(responseFromSubmissionEndpoint.getBody());
        }
        return responseFromSubmissionEndpoint;
    }

    /**
     * Creates the HTTP request instance to be sent to the POST submission endpoint
     * @param plainCode
     * @param languageCode
     * @return the HTTP request.
     */
    private SubmissionRequest createHttpSubmissionRequestFromCode(String plainCode, int languageCode) {
        SubmissionRequest request = new SubmissionRequest();

        String encodedCode = Base64.getEncoder()
                .encodeToString(plainCode.getBytes(StandardCharsets.UTF_8));
        request.setSourceCode(encodedCode);
        request.setLanguageId(languageCode);
        request.setStdin("SnVkZ2Uw");
        return request;
    }

    /**
     * Creates the HTTP headers to be used in the POST submission.
     * @return the headers.
     */
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("host", judge0Host);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * Post to the submission endpoint.
     * @param submissionRequest the submission request
     * @param requestHeaders the request headers
     * @return The response entity returned from the submission endpoint.
     */
    private ResponseEntity<String> postToSubmissionEndpoint(SubmissionRequest submissionRequest, HttpHeaders requestHeaders) {
        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(submissionRequest, requestHeaders);
        return restTemplate.exchange(
                judge0PostEndpoint + "?base64_encoded=true",
                HttpMethod.POST,
                entity,
                String.class);
    }

    /**
     * Gets the result of the submission after posting to the submission endpoint. This endpoint uses a queue and therefore
     * the result of the submission is not available immediately. I'm using a poll to work around this.
     * @param token the unique identifier of the submission
     * @return the final response entity
     */
    private ResponseEntity<String> getSubmissionResult(String token) {
        String url = judge0PostEndpoint + "/" + token + "?base64_encoded=true";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println(url);
        ResponseEntity<String> response = null;
        int attempt = 0;

        while (attempt < MAXIMUM_POLLING_ATTEMPTS) {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                int statusId = jsonNode.get("status").get("id").asInt();
                if (statusId != 1 && statusId != 2) {
                    System.out.println("Final submission status reached: " + jsonNode.get("status").get("description").asText());
                    break;
                }
            } catch (Exception e) {
                System.out.println("Failed to parse submission result: " + e.getMessage());
                e.printStackTrace();
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
        return response;
    }
}
