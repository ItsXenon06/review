package com.abc.backend.CNPM.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime; // Đổi import từ Instant sang LocalDateTime

@Entity
@Table(name = "VehicleCategory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Integer categoryId;

    @Column(name = "CategoryName", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "Description", length = 255)
    private String description;

    @Column(name = "BaseDailyRate", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseDailyRate;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt; // ĐỔI THÀNH kiểu LocalDateTime ở đây

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // Đổi hàm lấy thời gian tương ứng
        }
    }
}