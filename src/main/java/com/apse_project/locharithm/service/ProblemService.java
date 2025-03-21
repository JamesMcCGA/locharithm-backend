package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.ProblemDao;
import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.domain.TestCase;
import com.apse_project.locharithm.dtos.FullProblemDto;
import com.apse_project.locharithm.dtos.ProblemDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProblemService.class);
    
    private final ProblemDao problemDao;
    private final TestCasesService testCasesService;

    @Transactional(readOnly = true)
    public List<ProblemDto> getProblemDtos() {
        logger.debug("Fetching all problem DTOs");
        return problemDao.findAllProblemData().stream()
                .map(arr -> new ProblemDto((Integer) arr[0], (String) arr[1]))
                .toList();
    }

    public Optional<Problem> getProblem(int id) {
        return problemDao.findById(id);
    }

    @Transactional
    public void saveProblem(FullProblemDto fullProblemDto) {
        logger.info("Starting to save problem: {}", fullProblemDto.getProblemName());
        logger.debug("Building problem entity from DTO");
        Problem newProblem = Problem.builder()
                .name(fullProblemDto.getProblemName())
                .description(fullProblemDto.getProblemDescription())
                .inputConstraints(fullProblemDto.getInputConstraints())
                .outputConstraints(fullProblemDto.getOutputConstraints())
                .inputFormat(fullProblemDto.getInputFormat())
                .outputFormat(fullProblemDto.getOutputFormat())
                .sampleInput1(fullProblemDto.getSampleInput1())
                .sampleOutput1(fullProblemDto.getSampleOutput1())
                .sampleInput2(fullProblemDto.getSampleInput2())
                .sampleOutput2(fullProblemDto.getSampleOutput2())
                .testCaseInput1(fullProblemDto.getTestCaseInput1())
                .testCaseOutput1(fullProblemDto.getTestCaseOutput1())
                .testCaseInput2(fullProblemDto.getTestCaseInput2())
                .testCaseOutput2(fullProblemDto.getTestCaseOutput2())
                .testCaseInput3(fullProblemDto.getTestCaseInput3())
                .testCaseOutput3(fullProblemDto.getTestCaseOutput3())
                .testCaseInput4(fullProblemDto.getTestCaseInput4())
                .testCaseOutput4(fullProblemDto.getTestCaseOutput4())
                .testCaseInput5(fullProblemDto.getTestCaseInput5())
                .testCaseOutput5(fullProblemDto.getTestCaseOutput5())
                .build();
                
        logger.debug("Saving problem to database");
        Problem savedProblem = problemDao.save(newProblem);
        Integer problemId = savedProblem.getProblemId();
        logger.debug("Problem saved successfully with ID: {}", problemId);

        logger.debug("Creating test case 1");
        TestCase testCase1 = new TestCase();
        testCase1.setProblem(savedProblem);
        testCase1.setTestCaseInput(fullProblemDto.getTestCaseInput1());
        testCase1.setTestCaseOutput(fullProblemDto.getTestCaseOutput1());
        testCasesService.saveTestCase(problemId, testCase1);

        TestCase testCase2 = new TestCase();
        testCase2.setProblem(savedProblem);
        testCase2.setTestCaseInput(fullProblemDto.getTestCaseInput2());
        testCase2.setTestCaseOutput(fullProblemDto.getTestCaseOutput2());
        testCasesService.saveTestCase(problemId, testCase2);

        TestCase testCase3 = new TestCase();
        testCase3.setProblem(savedProblem);
        testCase3.setTestCaseInput(fullProblemDto.getTestCaseInput3());
        testCase3.setTestCaseOutput(fullProblemDto.getTestCaseOutput3());
        testCasesService.saveTestCase(problemId, testCase3);

        TestCase testCase4 = new TestCase();
        testCase4.setProblem(savedProblem);
        testCase4.setTestCaseInput(fullProblemDto.getTestCaseInput4());
        testCase4.setTestCaseOutput(fullProblemDto.getTestCaseOutput4());
        testCasesService.saveTestCase(problemId, testCase4);

        TestCase testCase5 = new TestCase();
        testCase5.setProblem(savedProblem);
        testCase5.setTestCaseInput(fullProblemDto.getTestCaseInput5());
        testCase5.setTestCaseOutput(fullProblemDto.getTestCaseOutput5());
        testCasesService.saveTestCase(problemId, testCase5);
    }
}
