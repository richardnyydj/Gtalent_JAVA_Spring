package com.example.demo.model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByContent(String content);
}
