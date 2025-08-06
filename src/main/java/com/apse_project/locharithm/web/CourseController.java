package com.apse_project.locharithm.web;

import com.apse_project.locharithm.domain.Course;
import com.apse_project.locharithm.domain.User;
import com.apse_project.locharithm.service.CourseParticipantService;
import com.apse_project.locharithm.service.CourseService;
import com.apse_project.locharithm.service.TemporaryPasswordService;
import com.apse_project.locharithm.service.UserService;
import com.apse_project.locharithm.util.CourseParticipantsCsvParser;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TemporaryPasswordService temporaryPasswordService;

    @PostMapping("/uploadCourse")
    public String uploadCourse(@RequestParam("file") MultipartFile file,
                               @RequestParam("courseName")  String courseName,
                               @RequestParam("instructorId") Integer instructorId) {


        // get and persist the course participants from the csv...
        List<User> courseParticipants = this.courseParticipantsCsvParser.parse(file);
        List<User> persistedUsers = this.userService.createUsersFromList(courseParticipants);

        // create the course entity itself...
        Course course = new Course();
        Optional<User> instructor = this.userService.getUserById(instructorId);
        course.setCourseName(courseName);
        Course persistedCourse = this.courseService.createCourse(course, instructor.get());

        System.out.println("Persisted course with ID: " + persistedCourse.getCourseId());
        return "bahright";
    }
}
