package com.apse_project.locharithm.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    private String userName;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "instructor")
    private Set<Course> coursesTaught;

    @OneToMany(mappedBy = "student")
    private Set<CourseParticipant> coursesParticipated;

}
