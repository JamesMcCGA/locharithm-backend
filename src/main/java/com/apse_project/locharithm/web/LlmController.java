package com.apse_project.locharithm.web;

import com.apse_project.locharithm.client.OpenApiClient;
import com.apse_project.locharithm.dtos.LlmRequestDto;
import com.apse_project.locharithm.service.Judge0ApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class LlmController {

    @Autowired
    private Judge0ApiService judge0ApiService;

    @Autowired
    private OpenApiClient openApiClient;

    public LlmController(Judge0ApiService judge0ApiService) {
        this.judge0ApiService = judge0ApiService;
    }

    @PostMapping("/queryLlm")
    public ResponseEntity<JsonArray> requestAi(@RequestBody LlmRequestDto llmRequestDto) {
        try {
            String query = llmRequestDto.getPrompt();
            ResponseEntity<String> apiResponse = openApiClient.sendChatMessage(query);
            String reply = apiResponse.getBody();
            JsonObject parsedResponse = openApiClient.parseResponse(reply);

            // Wrap the JSON object inside an array
            JsonArray array = new JsonArray();
            array.add(parsedResponse);

            return ResponseEntity.ok(array);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
