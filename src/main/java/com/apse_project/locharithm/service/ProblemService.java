package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.ProblemDao;
import com.apse_project.locharithm.domain.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemService {

    private final ProblemDao problemDao;

    public List<Problem> getProblems() {
        return (List<Problem>) problemDao.findAll();
    }

    public Optional<Problem> getProblem(int id) {
        return problemDao.findById(id);
    }

}
