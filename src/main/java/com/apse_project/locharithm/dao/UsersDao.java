package com.apse_project.locharithm.dao;

import com.apse_project.locharithm.domain.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends CrudRepository<Users, Integer> {

}
