package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.domain.Submission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionDao extends CrudRepository<Submission, Integer> {

}