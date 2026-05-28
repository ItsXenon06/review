package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Integer> {
    Optional<VehicleCategory> findByCategoryNameIgnoreCase(String categoryName);
}

