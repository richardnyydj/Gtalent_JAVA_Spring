package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends JpaRepository<DemoModel, Integer> {
    DemoModel findByName(String name);

    @Query("SELECT u FROM DemoModel u WHERE u.id = :id")
    DemoModel findByID(@Param("id") int id);
}
