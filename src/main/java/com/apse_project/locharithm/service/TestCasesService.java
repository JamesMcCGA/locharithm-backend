package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.TestCasesDao;
import com.apse_project.locharithm.dao.TestCasesDao;
import com.apse_project.locharithm.domain.TestCases;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCasesService {

    private final TestCasesDao testCaseDao;

    public TestCasesService(TestCasesDao testCaseDao) {
        this.testCaseDao = testCaseDao;
    }

    public List<TestCases> getAllTestCases() {
        return (List<TestCases>) testCaseDao.findAll();
    }

    public Optional<TestCases> getTestCaseById(Integer id) {
        return testCaseDao.findById(id);
    }

    public List<TestCases> getTestCasesByProblemId(Integer problemId) {
        return testCaseDao.findByProblem_ProblemId(problemId);
    }

    public TestCases saveTestCase(TestCases testCase) {
        return testCaseDao.save(testCase);
    }

    public void deleteTestCase(Integer id) {
        testCaseDao.deleteById(id);
    }
}
