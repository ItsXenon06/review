package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Integer> {

    List<InsurancePlan> findByIsActiveTrue();
}