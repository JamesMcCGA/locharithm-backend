package com.apse_project.locharithm.service;

import com.apse_project.locharithm.dao.UserDao;
import com.apse_project.locharithm.domain.User;
import com.apse_project.locharithm.domain.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserDao userDao;

    @Autowired
    private final TemporaryPasswordService temporaryPasswordService;

    @Transactional
    public Optional<User> getUserById(Integer userId) {
        logger.debug("Getting user by id {}", userId);
        return userDao.findById(userId);
    }

    @Transactional
    public Optional<User> getUserByEmail(String email) {
        logger.debug("Getting user by email {}", email);
        return userDao.findByUserEmail(email);
    }

    @Transactional
    public List<User> getUsersByRole(UserRole role) {
        logger.debug("Getting users by role {}", role);
        return userDao.findByRole(role);
    }

    @Transactional
    public User createUser(User user) {
        logger.debug("Creating new user with email {}", user.getUserEmail());
        String tempPass = this.temporaryPasswordService.generateTemporaryPassword();
        user.setPasswordHash(tempPass);
        user.setUserId(null);
        return userDao.save(user);
    }

    @Transactional
    public List<User> createUsersFromList(List<User> userList) {
        userList.forEach(u -> {
            logger.debug("Creating new user with email {}", u.getUserEmail());

            String tempPass = this.temporaryPasswordService.generateTemporaryPassword();
            u.setPasswordHash(tempPass);

            u.setUserId(null);
            userDao.save(u);
        });
        return userList;
    }

    @Transactional
    public User updateUser(User user) {
        logger.debug("Updating user with id {}", user.getUserId());
        return userDao.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        logger.debug("Deleting user with id {}", userId);
        userDao.deleteById(userId);
    }
}
