package com.apse_project.locharithm.service;

import com.apse_project.locharithm.client.Judge0ApiClient;
import com.apse_project.locharithm.domain.TestCase;
import com.apse_project.locharithm.dtos.SubmissionRequest;
import com.apse_project.locharithm.responses.Judge0ResponseParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Judge0ApiService {

    @Autowired
    private Judge0ApiClient judge0ApiClient;

    @Autowired
    private Judge0ResponseParser judge0ResponseParser;

    @Autowired
    private TestCasesService testCasesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Publicly accessible function to process a code submission end-to-end.
     */
    public ResponseEntity<String> submitCode(String plainCode, int problemId, int languageCode) {
        List<TestCase> testCases = testCasesService.getTestCasesByProblemId(problemId);
        Map<Integer, String> testResults = testCases.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        TestCase::getTestCaseNumber,
                        testCase -> {
                            try {
                                String input = testCase.getTestCaseInput();
                                String output = testCase.getTestCaseOutput();

                                SubmissionRequest request = judge0ApiClient.createHttpSubmissionRequestFromCode(
                                        plainCode,
                                        input,
                                        output,
                                        languageCode
                                );

                                HttpHeaders requestHeaders = judge0ApiClient.createHttpHeaders();
                                ResponseEntity<String> responseFromSubmissionEndpoint =
                                        judge0ApiClient.postToSubmissionEndpoint(request, requestHeaders);

                                if (responseFromSubmissionEndpoint.getStatusCode().is2xxSuccessful()) {
                                    String token = judge0ResponseParser.retrieveItemFromJsonBody(
                                            responseFromSubmissionEndpoint.getBody(),
                                            "token"
                                    );
                                    return judge0ApiClient.getSubmissionResult(token);
                                } else {
                                    return "Error: Submission Failed";
                                }
                            } catch (Exception e) {
                                return "Error: " + e.getMessage();
                            }
                        }
                ));

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
}
