package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.model.Notification; // Đảm bảo import đúng Model của bạn
import com.abc.backend.CNPM.repository.NotificationRepository; // Bạn cần tạo file Repository này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 1. Đếm tất cả thông báo chưa đọc (để hiển thị số 5 màu xanh)


    // 2. Lấy danh sách thông báo mới nhất (để hiển thị trong phần "Danh sách thông báo")
    public List<Notification> findLatestNotifications() {
        // Lấy 5-10 cái mới nhất, sắp xếp theo thời gian giảm dần
        return notificationRepository.findTop5ByOrderBySentAtDesc();
    }

    // 3. Đếm tất cả thông báo (để hiển thị số 38)
    public long countUnread() {
        return notificationRepository.countByIsReadFalse();
    }
}