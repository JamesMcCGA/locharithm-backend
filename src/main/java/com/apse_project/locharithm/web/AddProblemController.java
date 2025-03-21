package com.apse_project.locharithm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apse_project.locharithm.dtos.FullProblemDto;
import com.apse_project.locharithm.service.ProblemService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class AddProblemController {
    
    private static final Logger logger = LoggerFactory.getLogger(AddProblemController.class);

    @Autowired
    private ProblemService problemService;

    @PostMapping("/addProblem")
    public ResponseEntity<String> addProblem(@RequestBody FullProblemDto problemDto) {
        try {
            logger.info("Received request to add problem: {}", problemDto.getProblemName());
            problemService.saveProblem(problemDto);
            logger.info("Successfully added problem: {}", problemDto.getProblemName());
            return ResponseEntity.ok("Problem and test cases added successfully");
        } catch (Exception e) {
            logger.error("Error adding problem: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error adding problem: " + e.getMessage());
        }
    }
}
