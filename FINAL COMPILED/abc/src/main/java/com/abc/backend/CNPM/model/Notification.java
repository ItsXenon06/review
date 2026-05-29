package com.abc.backend.CNPM.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID")
    private Long notificationID;

    @Column(name = "ContractID")
    private Integer contractID;

    @Column(name = "Title", length = 200)
    private String title;

    @Column(name = "Content", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(name = "SentAt")
    private LocalDateTime sentAt;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "IsRead", nullable = false)
    private boolean isRead = false;

    @PrePersist
    protected void onCreate() {
        if (sentAt == null) sentAt = LocalDateTime.now();
    }
}
