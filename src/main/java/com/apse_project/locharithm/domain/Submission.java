package com.apse_project.locharithm.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name="submission")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Submission {

    @Id
    @Column(name="submission_id")
    private int submissionId;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name="submission_outcome")
    private String submissionOutcome;

    @Column(name="exec_time")
    private String execTime;

    @Column(name="memory_used")
    private String memoryUsed;

    @Column(name="language")
    private String language;

    @Column(name="code_size")
    private String codeSize;
}
