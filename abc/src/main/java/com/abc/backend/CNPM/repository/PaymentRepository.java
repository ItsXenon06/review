package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Payment;
import com.abc.backend.CNPM.model.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByReservationReservationId(Integer reservationId);

    List<Payment> findByRentalRentalId(Integer rentalId);

    List<Payment> findByPaymentType(PaymentType paymentType);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.rental.rentalId = :rentalId")
    BigDecimal sumAmountByRentalId(@Param("rentalId") Integer rentalId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.reservation.reservationId = :reservationId")
    BigDecimal sumAmountByReservationId(@Param("reservationId") Integer reservationId);
}

