package com.example.demo.model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemoRepository extends JpaRepository<DemoModel, Integer> {
    DemoModel findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT u FROM DemoModel u WHERE u.id = :id")
    DemoModel findByID(@Param("id") int id);
}