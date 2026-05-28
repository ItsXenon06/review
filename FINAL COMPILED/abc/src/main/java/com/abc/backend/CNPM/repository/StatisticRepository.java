package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface StatisticRepository extends JpaRepository<Payment, Integer> {

    @Query(value = "SELECT SUM(Amount) FROM Payment WHERE PaymentType = 'Charge'", nativeQuery = true)
    Double getTotalRevenue();

    @Query(value = "SELECT COUNT(*) FROM Reservation", nativeQuery = true)
    Long getTotalBookings();

    @Query(value = "SELECT Status, COUNT(*) as Count FROM Vehicle GROUP BY Status", nativeQuery = true)
    List<Map<String, Object>> getVehicleStatusStats();

    @Query(value = """
        SELECT MONTH(PaymentDate) as Month, SUM(Amount) as Revenue
        FROM Payment WHERE PaymentType = 'Charge' AND YEAR(PaymentDate) = YEAR(GETDATE())
        GROUP BY MONTH(PaymentDate) ORDER BY Month
        """, nativeQuery = true)
    List<Map<String, Object>> getMonthlyRevenueStats();

    @Query(value = """
        SELECT TOP 5 v.Make + ' ' + v.Model as Model, COUNT(r.RentalID) as Count
        FROM Rental r
        JOIN Reservation res ON r.ReservationID = res.ReservationID
        JOIN Vehicle v ON res.VehicleID = v.VehicleID
        GROUP BY v.Make, v.Model ORDER BY Count DESC
        """, nativeQuery = true)
    List<Map<String, Object>> getTopVehicles();

    @Query(value = "SELECT Status, COUNT(*) as Count FROM Reservation GROUP BY Status", nativeQuery = true)
    List<Map<String, Object>> getReservationStatusStats();
}