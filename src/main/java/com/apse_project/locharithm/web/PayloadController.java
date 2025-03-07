package com.apse_project.locharithm.web;

import com.apse_project.locharithm.dtos.PayloadDto;
import com.apse_project.locharithm.service.Judge0ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PayloadController {

    @Autowired
    private Judge0ApiService judge0ApiService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitCode(@RequestBody PayloadDto payload) {
        String code = payload.getSource_code();
        int problemId = payload.getProblem_id();
        int languageCode = payload.getLanguage_id();
        System.out.println(code);
        System.out.println(problemId);
        System.out.println(languageCode);
        return judge0ApiService.submitCode(code, problemId, languageCode);
    }
}
