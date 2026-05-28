package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    /** Used by the calculation page to list all non-retired vehicles with category loaded. */
    @Query("SELECT v FROM Vehicle v JOIN FETCH v.category WHERE v.status <> :status")
    List<Vehicle> findByStatusNot(VehicleStatus status);

    List<Vehicle> findByStatus(VehicleStatus status);
}