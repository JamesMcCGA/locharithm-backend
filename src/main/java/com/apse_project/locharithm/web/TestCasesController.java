package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.TestCases;
import com.apse_project.locharithm.service.TestCasesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testcases") //
public class TestCasesController {

    private final TestCasesService testCasesService;

    public TestCasesController(TestCasesService testCasesService) {
        this.testCasesService = testCasesService;
    }

    @GetMapping
    public List<TestCases> getAllTestCases() {
        return testCasesService.getAllTestCases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCases> getTestCaseById(@PathVariable Integer id) {
        Optional<TestCases> testCase = testCasesService.getTestCaseById(id);
        return testCase.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/problem/{problemId}")
    public List<TestCases> getTestCasesByProblemId(@PathVariable Integer problemId) {
        return testCasesService.getTestCasesByProblemId(problemId);
    }

    @PostMapping
    public TestCases createTestCase(@RequestBody TestCases testCase) {
        return testCasesService.saveTestCase(testCase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Integer id) {
        testCasesService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }
}
