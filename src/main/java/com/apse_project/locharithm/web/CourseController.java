package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.Course;
import com.apse_project.locharithm.domain.CourseParticipant;
import com.apse_project.locharithm.domain.User;
import com.apse_project.locharithm.service.CourseParticipantService;
import com.apse_project.locharithm.service.CourseService;
import com.apse_project.locharithm.service.TemporaryPasswordService;
import com.apse_project.locharithm.service.UserService;
import com.apse_project.locharithm.util.CourseParticipantsCsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins="*")
public class CourseController {

    @Autowired
    private CourseParticipantsCsvParser courseParticipantsCsvParser;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService  courseService;

    @Autowired
    private CourseParticipantService courseParticipantService;

    @PostMapping("/uploadCourse")
    public ResponseEntity<String> uploadCourse(@RequestParam("file") MultipartFile file,
                                               @RequestParam("courseName")  String courseName,
                                               @RequestParam("instructorId") Integer instructorId) {

        // create the course entity itself...
        Course course = new Course();
        Optional<User> instructor = this.userService.getUserById(instructorId); // assumes the instructor is already an existing user when the course is created
        course.setCourseName(courseName);
        Course persistedCourse = this.courseService.createCourse(course, instructor.get());

        // get and create the users from the csv...
        List<User> courseStudents = this.courseParticipantsCsvParser.parse(file);
        List<User> persistedUsers = this.userService.createUsersFromList(courseStudents);


        // create the course entity linking the course and user
        persistedUsers.forEach(persistedUser -> {
            CourseParticipant courseParticipant = new CourseParticipant();
            courseParticipant.setStudent(persistedUser);
            courseParticipant.setCourse(persistedCourse);
            this.courseParticipantService.createCourseParticipant(courseParticipant);
        });

        return ResponseEntity.ok("Course created successfully! ");
    }
}
