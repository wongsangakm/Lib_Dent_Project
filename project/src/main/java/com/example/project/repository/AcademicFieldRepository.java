package com.example.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.AcademicField;

public interface AcademicFieldRepository extends JpaRepository<AcademicField, Long> {
    Optional<AcademicField> findByCode(String code);
}

