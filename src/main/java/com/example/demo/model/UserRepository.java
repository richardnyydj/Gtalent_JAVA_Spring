package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(int id);

    @EntityGraph(attributePaths = {"posts"})
    @Query("SELECT u FROM users u WHERE u.id = :id")
    Optional<User> findByIdWithPosts(@Param("id") int id);
}