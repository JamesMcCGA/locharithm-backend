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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_case_id", columnDefinition = "INTEGER PRIMARY KEY AUTOINCREMENT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "test_case_input_1", nullable = false)
    private String testCaseInput1;

    @Column(name = "test_case_input_2")
    private String testCaseInput2;

    @Column(name = "test_case_input_3")
    private String testCaseInput3;

    @Column(name = "test_case_input_4")
    private String testCaseInput4;

    @Column(name = "test_case_input_5")
    private String testCaseInput5;

    @Column(name = "test_case_output_1", nullable = false)
    private String testCaseOutput1;

    @Column(name = "test_case_output_2")
    private String testCaseOutput2;

    @Column(name = "test_case_output_3")
    private String testCaseOutput3;

    @Column(name = "test_case_output_4")
    private String testCaseOutput4;

    @Column(name = "test_case_output_5")
    private String testCaseOutput5;
}
