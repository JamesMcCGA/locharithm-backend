package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.TestCaseDao;
import com.apse_project.locharithm.domain.TestCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCasesService {

    private final TestCaseDao testCaseDao;

    public TestCasesService(TestCaseDao testCaseDao) {
        this.testCaseDao = testCaseDao;
    }

    public List<TestCase> getAllTestCases() {
        return (List<TestCase>) testCaseDao.findAll();
    }

    public Optional<TestCase> getTestCaseById(Integer id) {
        return testCaseDao.findById(id);
    }

    public List<TestCase> getTestCasesByProblemId(Integer problemId) {
        return testCaseDao.findByProblem_ProblemId(problemId);
    }

    public TestCase saveTestCase(TestCase testCase) {
        return testCaseDao.save(testCase);
    }

    public void deleteTestCase(Integer id) {
        testCaseDao.deleteById(id);
    }
}
