package com.apse_project.locharithm;

import com.apse_project.locharithm.dao.ProblemDao;
import com.apse_project.locharithm.domain.Problem;
import com.apse_project.locharithm.dtos.ProblemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProblemDaoTest {

    @Autowired
    private ProblemDao underTest;

    @Test
    void findById(){
        Problem expected = Problem.builder()
                .problemId(1)
                .name("problem1")
                .description("This is problem 1")
                .sampleInput("10")
                .sampleOutput("20")
                .build();

        Problem actual = underTest.findById(1).orElse(null);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void findAll(){

        Iterable<Problem> allProblems = underTest.findAll();

        assertThat(allProblems).hasSize(2);

    }

    @Test
    void findAllProblemData(){
        List<ProblemDto> expected = List.of(new ProblemDto(1, "problem1"), new ProblemDto(2, "problem2"));

        List<ProblemDto> actual = underTest.findAllProblemData().stream()
                .map(arr -> new ProblemDto((Integer) arr[0], (String) arr[1]))
                .toList();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
