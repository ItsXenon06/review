package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Vehicle;
import com.abc.backend.CNPM.model.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByStatus(VehicleStatus status);

    List<Vehicle> findByCategoryCategoryId(Integer categoryId);

    List<Vehicle> findByStatusAndCategoryCategoryId(VehicleStatus status, Integer categoryId);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Optional<Vehicle> findByVin(String vin);

    /** Vehicles NOT in any active (Pending/Confirmed/Active) reservation for the date range. */
    @Query("""
        SELECT v FROM Vehicle v
        WHERE v.status = 'Available'
          AND v.vehicleId NOT IN (
              SELECT r.vehicle.vehicleId FROM Reservation r
              WHERE r.status IN ('Pending','Confirmed','Active')
                AND r.pickupDate < :returnDate
                AND r.returnDate > :pickupDate
          )
    """)
    List<Vehicle> findAvailableForDateRange(
            @Param("pickupDate") LocalDate pickupDate,
            @Param("returnDate") LocalDate returnDate);

    /** Count of vehicles currently rented (for demand-pricing occupancy calc). */
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = 'Rented'")
    long countRented();

    long countByStatusNot(VehicleStatus status);
}

