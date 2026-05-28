package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.dto.MaintenanceDTO;
import com.abc.backend.CNPM.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Integer> {

    List<MaintenanceRecord> findByVehicleVehicleIdOrderByServiceDateDesc(Integer vehicleId);

    @Query("""
        SELECT new com.abc.backend.CNPM.dto.MaintenanceDTO(
            m.vehicle.vehicleId, m.vehicle.make, m.vehicle.licensePlate,
            m.description, m.nextServiceDate)
        FROM MaintenanceRecord m
        WHERE m.nextServiceDate >= :today
        ORDER BY m.nextServiceDate ASC
    """)
    List<MaintenanceDTO> findUpcomingMaintenance(@Param("today") LocalDate today);
}
