package com.example.itss.repository;

import com.example.itss.domain.model.School;
import com.example.itss.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SchoolRepository extends JpaRepository<School, Integer>, JpaSpecificationExecutor<School> {
    boolean existsByEmail(String email);
    Optional<School> findByEmail(String email);
}