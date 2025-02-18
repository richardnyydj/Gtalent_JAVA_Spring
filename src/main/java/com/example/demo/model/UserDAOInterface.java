package com.example.demo.model;

import org.springframework.data.repository.query.Param;

public interface UserDAOInterface {
    User findById(int id);
    User findUserByUsername(String username);
    boolean updatePassword(String username, String password);
    User findUserWithPosts(@Param("id") int id);
}