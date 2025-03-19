package com.apse_project.locharithm.web;

import com.apse_project.locharithm.client.OpenApiClient;
import com.apse_project.locharithm.dtos.LlmRequestDto;
import com.apse_project.locharithm.service.Judge0ApiService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> requestAi(@RequestBody LlmRequestDto llmRequestDto) throws IOException, InterruptedException {
        String query = llmRequestDto.getPrompt();
        return openApiClient.sendChatMessage(query);
    }
}
