package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.CourseParticipant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseParticipantDao extends CrudRepository<CourseParticipant, Integer> {

    List<CourseParticipant> findByCourse_CourseId(Integer courseId);

    List<CourseParticipant> findByStudent_UserId(Integer studentId);

}
