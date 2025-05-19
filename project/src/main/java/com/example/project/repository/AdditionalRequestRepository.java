package com.example.project.repository;

import com.example.project.model.AdditionalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalRequestRepository extends JpaRepository<AdditionalRequest, Long> {
}