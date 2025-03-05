package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.Problem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemDao extends CrudRepository<Problem, Integer> {

}
