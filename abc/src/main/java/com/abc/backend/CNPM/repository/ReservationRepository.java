package com.abc.backend.CNPM.repository;


import com.abc.backend.CNPM.model.Reservation;
import com.abc.backend.CNPM.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByCustomerCustomerId(Integer customerId);

    List<Reservation> findByVehicleVehicleId(Integer vehicleId);

    List<Reservation> findByStatus(ReservationStatus status);

    /** Active reservations overlapping a date range (for availability checks). */
    List<Reservation> findByVehicleVehicleIdAndStatusInAndPickupDateBeforeAndReturnDateAfter(
            Integer vehicleId,
            List<ReservationStatus> statuses,
            LocalDate returnDate,
            LocalDate pickupDate);
}

