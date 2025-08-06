package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.dtos.FullProblemDto;
import com.apse_project.locharithm.dtos.ProblemDto;
import com.apse_project.locharithm.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/problems/")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class ProblemController {

    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);

    @Autowired
    private final ProblemService problemService;

    // GET
    @GetMapping
    public List<ProblemDto> getProblemDtos() {
        return problemService.getProblemDtos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Integer id) {
        Optional<Problem> problem = problemService.getProblem(id);
        return problem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST
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
