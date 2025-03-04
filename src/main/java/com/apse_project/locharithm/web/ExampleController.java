package com.apse_project.locharithm.web;

import com.apse_project.locharithm.dao.UsersDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path="example/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExampleController {

    private final UsersDao usersDao;

    @GetMapping(path="users/", params="id")
    public ResponseEntity<Map<String, String>> getUser(@RequestParam("id") int id) {
        return usersDao.findById(id)
                .map(user -> ResponseEntity.ok(Collections.singletonMap("name", user.getName())))
                .orElse(ResponseEntity.notFound().build());
    }
}


