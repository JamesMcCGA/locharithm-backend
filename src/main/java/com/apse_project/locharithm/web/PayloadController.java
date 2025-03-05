package com.apse_project.locharithm.web;

import com.apse_project.locharithm.dtos.Payload;
import com.apse_project.locharithm.service.Judge0ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PayloadController {

    @Autowired
    private Judge0ApiService judge0ApiService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitCode(@RequestBody Payload payload) {
        String code = payload.getSourceCode();
        int languageCode = payload.getLanguageCode();
        return judge0ApiService.submitCode(code, languageCode);
    }
}
