package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.TestCases;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCasesDao extends CrudRepository<TestCases, Integer> {
    List<TestCases> findByProblem_ProblemId(Integer problemId);
}
