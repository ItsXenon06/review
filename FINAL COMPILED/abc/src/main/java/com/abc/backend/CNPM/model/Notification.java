package com.abc.backend.CNPM.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification") // Tên bảng trong SQL
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID") // Map đúng tên cột trong SQL
    private Long notificationID;

    @Column(name = "ContractID")
    private Long contractID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Content")
    private String content;

    @Column(name = "SentAt")
    private LocalDateTime sentAt;

    @Column(name = "Status")
    private String status;
    @Column(name = "IsRead")
    private boolean isRead;

    // ... thêm Getter & Setter ...

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
    // Constructor rỗng (Bắt buộc phải có cho JPA)
    public Notification() {}

    // --- CÁCH TẠO GETTER & SETTER ---
    // Đừng dùng @Data nữa.
    // Trong IntelliJ, bạn nhấn chuột phải vào khoảng trống trong class -> Chọn "Generate..." -> "Getter and Setter" -> Chọn tất cả -> OK.
    // IDE sẽ tự động viết code cho bạn, lỗi đỏ sẽ biến mất hoàn toàn.

    public Long getNotificationID() { return notificationID; }
    public void setNotificationID(Long notificationID) { this.notificationID = notificationID; }

    public Long getContractID() { return contractID; }
    public void setContractID(Long contractID) { this.contractID = contractID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}