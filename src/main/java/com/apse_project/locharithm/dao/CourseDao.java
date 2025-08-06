package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends CrudRepository<Course, Integer> {

    List<Course> findByCourseName(String courseName);

    List<Course> findByInstructor_UserId(Integer instructorId);
}
