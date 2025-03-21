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
    @Column(name = "test_case_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "test_case_input", nullable = false)
    private String testCaseInput;

    @Column(name = "test_case_output", nullable = false)
    private String testCaseOutput;

    @Column(name = "test_case_number", nullable = false)
    private Integer testCaseNumber;
}

