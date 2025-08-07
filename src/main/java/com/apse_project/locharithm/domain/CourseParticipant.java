package com.apse_project.locharithm.domain;

import com.apse_project.locharithm.domain.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "course_participants")
public class CourseParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseParticipantId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
