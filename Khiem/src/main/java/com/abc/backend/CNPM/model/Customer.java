package com.abc.backend.CNPM.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Customer")
@Data // Lombok sẽ tự động tạo getter, setter, toString, hashCode, equals
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerID;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "DriverLicenseNo")
    private String driverLicenseNo;

    @Column(name = "LicenseExpiry")
    private LocalDate licenseExpiry;

    @Column(name = "LicenseCountry")
    private String licenseCountry;
}