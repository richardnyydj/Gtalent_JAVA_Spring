package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 因已經繼承了Spring Data提供的接口，例如 JpaRepository，不需要額外加 @Repository，因為 Spring Data JPA 會自動識別。
public interface DemoRepository extends JpaRepository<DemoModel, Integer> {
    DemoModel findByName(String name);

    DemoModel findById(int id);

    @Query("SELECT u FROM DemoModel u WHERE u.id = :id")
    DemoModel findByID(@Param("id") int id);
}
