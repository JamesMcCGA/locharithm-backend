package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.User;
import com.apse_project.locharithm.domain.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

    Optional<User> findByUserEmail(String email);

    List<User> findByRole(UserRole role);

    Optional<User> findByUserId(Integer id);
}
