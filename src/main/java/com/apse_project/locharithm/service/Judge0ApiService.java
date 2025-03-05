package com.apse_project.locharithm.service;

import com.apse_project.locharithm.domain.TestCases;
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
import java.util.HashMap;
import java.util.List;

@Service
public class Judge0ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestCasesService testCasesService;

    // from spring properties
    @Value("${judge0.api.postEndpoint}")
    private String judge0PostEndpoint;

    @Value("${judge0.api.host}")
    private String judge0Host;

    private static final int POLLING_INTERVAL_IN_MILLIS = 1000;

    private static final int MAXIMUM_POLLING_ATTEMPTS = 5;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Publicly accessible function to process a code submission end-to-end.
     */
    public ResponseEntity<String> submitCode(String plainCode, int problemId, int languageCode) {
        List<TestCases> testCases = testCasesService.getTestCasesByProblemId(problemId);

        HashMap<Integer, String> testResults = new HashMap<>();
        for (TestCases testCase : testCases) {
            String stdinFormatted = testCase.getTestCaseInput().trim() + "\n";
            String expectedOutputFormatted = testCase.getTestCaseOutput().trim() + "\n";

            SubmissionRequest request = createHttpSubmissionRequestFromCode(plainCode, stdinFormatted, expectedOutputFormatted, languageCode);
            HttpHeaders requestHeaders = createHttpHeaders();
            ResponseEntity<String> responseFromSubmissionEndpoint = postToSubmissionEndpoint(request, requestHeaders);

            if (responseFromSubmissionEndpoint.getStatusCode().is2xxSuccessful()) {
                String body = responseFromSubmissionEndpoint.getBody();

                try {
                    JsonNode jsonNode = objectMapper.readTree(body);
                    String token = jsonNode.get("token").asText();

                    ResponseEntity<String> finalResponse = getSubmissionResult(token, problemId);
                    JsonNode responseJsonNode = objectMapper.readTree(finalResponse.getBody());

                    String acceptanceStatus = responseJsonNode.get("status").get("description").asText();
                    testResults.put(testCase.getId(), acceptanceStatus);
                    System.out.println(acceptanceStatus);

                } catch (Exception e) {
                    System.out.println("Failed to extract token from JSON: " + e.getMessage());
                    e.printStackTrace();
                    testResults.put(testCase.getId(), "Error: Failed to parse response");
                }

                System.out.println("Test case " + testCase.getId() + " processed");
            } else {
                System.out.println("Failed to submit code: " + responseFromSubmissionEndpoint.getStatusCode());
                System.out.println(responseFromSubmissionEndpoint.getBody());
                testResults.put(testCase.getId(), "Error: Submission Failed");
            }
        }

        String jsonResults = "{}";
        try {
            jsonResults = objectMapper.writeValueAsString(testResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResults);
    }

    /**
     * Creates the HTTP request instance to be sent to the POST submission endpoint.
     */
    private SubmissionRequest createHttpSubmissionRequestFromCode(String plainCode, String stdin, String expectedOutput, int languageCode) {
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
     * Creates the HTTP headers to be used in the POST submission.
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
     * Gets the result of the submission after posting to the submission endpoint.
     */
    private ResponseEntity<String> getSubmissionResult(String token, int problem_id) {
        String url = judge0PostEndpoint + "/" + token + "?base64_encoded=true";
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = null;
        int attempt = 0;

        while (attempt < MAXIMUM_POLLING_ATTEMPTS) {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                int statusId = jsonNode.get("status").get("id").asInt();
                if (statusId != 1 && statusId != 2) {
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
