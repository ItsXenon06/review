package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.DamageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReport, Integer> {

    List<DamageReport> findByRentalRentalId(Integer rentalId);

    List<DamageReport> findByCustomerLiableTrue();
}
