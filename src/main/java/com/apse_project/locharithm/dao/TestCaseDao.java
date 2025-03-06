package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.TestCase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseDao extends CrudRepository<TestCase, Integer> {
    List<TestCase> findByProblem_ProblemId(Integer problemId);
}
