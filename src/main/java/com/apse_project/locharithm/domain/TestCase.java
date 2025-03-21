package com.apse_project.locharithm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "testcase_gen")
    @TableGenerator(name = "testcase_gen", 
                   table = "id_generator", 
                   pkColumnName = "gen_name", 
                   valueColumnName = "gen_value",
                   pkColumnValue = "testcase_id",
                   initialValue = 1,
                   allocationSize = 1)
    @Column(name = "test_case_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "test_case_input", nullable = false)
    private String testCaseInput;

    @Column(name = "test_case_output", nullable = false)
    private String testCaseOutput;
}
