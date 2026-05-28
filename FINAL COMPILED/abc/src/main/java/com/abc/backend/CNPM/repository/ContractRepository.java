package com.abc.backend.CNPM.repository;

import com.abc.backend.CNPM.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("SELECT c FROM Contract c WHERE c.endDate IS NOT NULL ORDER BY c.endDate ASC")
    List<Contract> findAllOrderByEndDateAsc();
}
