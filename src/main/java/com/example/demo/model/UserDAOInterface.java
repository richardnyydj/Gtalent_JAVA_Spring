package com.example.demo.model;

public interface UserDAOInterface {
    User findById(int id);
    User findUserByUsername(String username);
    boolean updatePassword(String username, String password);
    User findUserWithPostsById(int id);
}