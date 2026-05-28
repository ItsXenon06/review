package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.Notification;
import com.abc.backend.CNPM.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications") // Đổi từ /api/contracts thành /api/notifications
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // URL sẽ là: POST /api/notifications/send/{id}
    @PostMapping("/send/{id}")
    public ResponseEntity<String> sendNotification(@PathVariable Long id) {
        try {
            // Logic tạo và lưu thông báo
            Notification notification = new Notification();
            notification.setContractID(id);
            notification.setTitle("Thông báo nhắc nhở");
            notification.setContent("Hợp đồng " + id + " đã đến hạn xử lý.");
            notification.setSentAt(LocalDateTime.now());
            notification.setStatus("Sent");

            notificationRepository.save(notification);

            return ResponseEntity.ok("Đã gửi thông báo và lưu log thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi gửi thông báo: " + e.getMessage());
        }
    }
}