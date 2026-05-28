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

    long countByStatus(VehicleStatus status);

    long countByStatusNot(VehicleStatus status);

    @Query("""
        SELECT v FROM Vehicle v
        WHERE (:tuKhoa IS NULL OR
                LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))
             OR LOWER(v.make)         LIKE LOWER(CONCAT('%', :tuKhoa, '%'))
             OR LOWER(v.model)        LIKE LOWER(CONCAT('%', :tuKhoa, '%')))
          AND (:hangXe    IS NULL OR v.make  = :hangXe)
          AND (:moHinh    IS NULL OR v.model = :moHinh)
          AND (:trangThai IS NULL OR v.status = :trangThaiEnum)
    """)
    List<Vehicle> filterVehicles(
            @Param("tuKhoa")       String tuKhoa,
            @Param("hangXe")       String hangXe,
            @Param("moHinh")       String moHinh,
            @Param("trangThai")    String trangThai,
            @Param("trangThaiEnum") VehicleStatus trangThaiEnum);

    @Query("""
        SELECT v FROM Vehicle v
        WHERE v.status = 'Available'
          AND v.vehicleId NOT IN (
              SELECT r.vehicle.vehicleId FROM Reservation r
              WHERE r.status IN ('Pending','Confirmed','Active')
                AND r.pickupDate < :ngayTra
                AND r.returnDate > :ngayNhan
          )
    """)
    List<Vehicle> findAvailableForDateRange(
            @Param("ngayNhan") LocalDate ngayNhan,
            @Param("ngayTra")  LocalDate ngayTra);

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = 'Rented'")
    long countRented();
}
