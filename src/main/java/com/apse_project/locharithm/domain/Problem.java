package com.apse_project.locharithm.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="problem")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "problem_gen")
    @TableGenerator(name = "problem_gen", 
                   table = "id_generator", 
                   pkColumnName = "gen_name", 
                   valueColumnName = "gen_value",
                   pkColumnValue = "problem_id",
                   initialValue = 1,
                   allocationSize = 1)
    @Column(name="problem_id")
    private Integer problemId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="input_constraints")
    private String inputConstraints;

    @Column(name="output_constraints")
    private String outputConstraints;

    @Column(name="input_format")
    private String inputFormat;

    @Column(name="output_format")
    private String outputFormat;

    @Column(name="sample_input1")
    private String sampleInput1;

    @Column(name="sample_output1")
    private String sampleOutput1;

    @Column(name="sample_input2")
    private String sampleInput2;

    @Column(name="sample_output2")
    private String sampleOutput2;

    @Column(name="test_case_input1")
    private String testCaseInput1;

    @Column(name="test_case_output1")
    private String testCaseOutput1;

    @Column(name="test_case_input2")
    private String testCaseInput2;

    @Column(name="test_case_output2")
    private String testCaseOutput2;

    @Column(name="test_case_input3")
    private String testCaseInput3;

    @Column(name="test_case_output3")
    private String testCaseOutput3;

    @Column(name="test_case_input4")
    private String testCaseInput4;

    @Column(name="test_case_output4")
    private String testCaseOutput4;

    @Column(name="test_case_input5")
    private String testCaseInput5;

    @Column(name="test_case_output5")
    private String testCaseOutput5;
}
