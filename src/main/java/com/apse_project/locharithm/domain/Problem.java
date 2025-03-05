package com.apse_project.locharithm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name="problem_id")
    private int problemId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="sample_input")
    private String sampleInput;

    @Column(name="sample_output")
    private String sampleOutput;
}
