package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemDao extends CrudRepository<Problem, Integer> {
    @Query("SELECT p.problemId, p.name FROM Problem p")
    List<Object[]> findAllProblemData();
}
