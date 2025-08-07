package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.CourseParticipantDao;
import com.apse_project.locharithm.domain.CourseParticipant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseParticipantService {
    private static final Logger logger = LoggerFactory.getLogger(CourseParticipantService.class);
    private final CourseParticipantDao courseParticipantDao;

    @Transactional
    public Optional<CourseParticipant> getById(Integer id) {
        logger.debug("Getting course participant by ID: {}", id);
        return courseParticipantDao.findById(id);
    }

    @Transactional
    public List<CourseParticipant> getParticipantsByCourseId(Integer courseId) {
        logger.debug("Getting participants for course ID: {}", courseId);
        return courseParticipantDao.findByCourse_CourseId(courseId);
    }

    @Transactional
    public List<CourseParticipant> getParticipantsByUserId(Integer userId) {
        logger.debug("Getting courses for user ID: {}", userId);
        return courseParticipantDao.findByStudent_UserId(userId);
    }

    @Transactional
    public CourseParticipant createCourseParticipant(CourseParticipant participant) {
        logger.debug("Creating course participant for user ID {} in course ID {}",
                participant.getStudent().getUserId(), participant.getCourse().getCourseId());
        participant.setCourseParticipantId(null);
        return courseParticipantDao.save(participant);
    }

    @Transactional
    public CourseParticipant updateCourseParticipant(CourseParticipant participant) {
        logger.debug("Updating course participant with ID: {}", participant.getCourseParticipantId());
        return courseParticipantDao.save(participant);
    }

    @Transactional
    public void deleteCourseParticipant(Integer id) {
        logger.debug("Deleting course participant with ID: {}", id);
        courseParticipantDao.deleteById(id);
    }
}
