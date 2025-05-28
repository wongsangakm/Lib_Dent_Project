package com.example.project.repository;

import com.example.project.model.AdditionalRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalRequestRepository extends JpaRepository<AdditionalRequest, Long> {
    List<AdditionalRequest> findByRequestedBy(String requestedBy);
    List<AdditionalRequest> findAll();

}
