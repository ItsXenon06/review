package com.abc.backend.CNPM.repository;


import com.abc.backend.CNPM.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByDriverLicenseNo(String driverLicenseNo);

    boolean existsByEmail(String email);

    boolean existsByDriverLicenseNo(String driverLicenseNo);
}

