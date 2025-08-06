package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.CourseDao;
import com.apse_project.locharithm.domain.Course;
import com.apse_project.locharithm.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseDao courseDao;

    @Transactional
    public Optional<Course> getCourseById(Integer courseId) {
        logger.debug("Getting course by ID: {}", courseId);
        return courseDao.findById(courseId);
    }

    @Transactional
    public List<Course> getCoursesByName(String name) {
        logger.debug("Getting courses by name: {}", name);
        return courseDao.findByCourseName(name);
    }

    @Transactional
    public List<Course> getCoursesByInstructorId(Integer instructorId) {
        logger.debug("Getting courses by instructor ID: {}", instructorId);
        return courseDao.findByInstructor_UserId(instructorId);
    }

    @Transactional
    public Course createCourse(Course course, User instructor) {
        logger.debug("Creating new course with name: {}", course.getCourseName());
        course.setInstructor(instructor);
        course.setCourseId(null);
        return courseDao.save(course);
    }

    @Transactional
    public Course updateCourse(Course course) {
        logger.debug("Updating course with ID: {}", course.getCourseId());
        return courseDao.save(course);
    }

    @Transactional
    public void deleteCourse(Integer courseId) {
        logger.debug("Deleting course with ID: {}", courseId);
        courseDao.deleteById(courseId);
    }
}
