package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    Optional<Rental> findByReservationReservationId(Integer reservationId);

    /** All open rentals (no return date yet). */
    List<Rental> findByActualReturnDateIsNull();
}
