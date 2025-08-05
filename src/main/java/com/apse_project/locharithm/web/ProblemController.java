package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.dtos.ProblemDto;
import com.apse_project.locharithm.service.ProblemService;
import lombok.RequiredArgsConstructor;
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

    private final ProblemService problemService;

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

}
