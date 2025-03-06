package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/problems/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public List<Problem> getProblems() {
        return problemService.getProblems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Integer id) {
        Optional<Problem> problem = problemService.getProblem(id);
        return problem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
