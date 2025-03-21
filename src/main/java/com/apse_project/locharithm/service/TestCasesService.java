package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.TestCaseDao;
import com.apse_project.locharithm.domain.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TestCasesService {

    private static final Logger logger = LoggerFactory.getLogger(TestCasesService.class);
    private final TestCaseDao testCaseDao;

    public TestCasesService(TestCaseDao testCaseDao) {
        this.testCaseDao = testCaseDao;
    }

    @Transactional(readOnly = true)
    public List<TestCase> getAllTestCases() {
        logger.debug("Fetching all test cases");
        return (List<TestCase>) testCaseDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TestCase> getTestCaseById(Integer id) {
        logger.debug("Fetching test case with id: {}", id);
        return testCaseDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TestCase> getTestCasesByProblemId(Integer problemId) {
        logger.debug("Fetching test cases for problem id: {}", problemId);
        return testCaseDao.findByProblem_ProblemId(problemId);
    }

    @Transactional
    public TestCase saveTestCase(int problemId, TestCase testCase) {
        logger.debug("Saving test case for problem id: {}", problemId);
        try {
            TestCase savedTestCase = testCaseDao.save(testCase);
            logger.debug("Successfully saved test case with id: {}", savedTestCase.getId());
            return savedTestCase;
        } catch (Exception e) {
            logger.error("Error saving test case: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteTestCase(Integer id) {
        logger.debug("Deleting test case with id: {}", id);
        testCaseDao.deleteById(id);
    }
}
