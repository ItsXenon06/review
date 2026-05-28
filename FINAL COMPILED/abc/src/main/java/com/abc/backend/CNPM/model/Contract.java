package com.abc.backend.CNPM.model;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Contract")
@Data
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractId")
    private Integer contractId;

    // Quan hệ với Customer - Hibernate sẽ tự tìm cột CustomerID dựa trên @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CustomerID")
    private Customer customer;

    @Column( name = "StartDate")
    private LocalDateTime startDate;

    @Column( name = "EndDate")
    private LocalDateTime endDate;

    @Transient
    public String getDaysRemainingStatus() {
        if (this.endDate == null) return "Chưa xác định";

        // Tính khoảng cách ngày giữa hôm nay và ngày kết thúc
        long days = ChronoUnit.DAYS.between(LocalDate.now(), this.endDate.toLocalDate());

        if (days < 0) {
            return "Hợp đồng hết hạn - Quá hạn " + Math.abs(days) + " ngày";
        } else if (days == 0) {
            return "Hạn ngày hôm nay";
        } else {
            return "Hạn còn " + days + " ngày";
        }
    }
    public boolean isExpired() {
        if (this.endDate == null) return false;
        return this.endDate.isBefore(java.time.LocalDateTime.now());
    }

    // Kiểm tra xem hợp đồng có hết hạn ngay hôm nay không
    public boolean isDueToday() {
        if (this.endDate == null) return false;
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate end = this.endDate.toLocalDate();
        return end.isEqual(today);
    }

}