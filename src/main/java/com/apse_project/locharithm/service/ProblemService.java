package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.ProblemDao;
import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.dtos.ProblemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemService {

    private final ProblemDao problemDao;

    public List<ProblemDto> getProblemDtos() {
        return problemDao.findAllProblemData().stream()
                .map(arr -> new ProblemDto((Integer) arr[0], (String) arr[1]))
                .toList();
    }

    public Optional<Problem> getProblem(int id) {return problemDao.findById(id);}

}
