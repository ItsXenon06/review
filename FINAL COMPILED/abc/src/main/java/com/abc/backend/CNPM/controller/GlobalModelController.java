package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelController {

    private final NotificationService notificationService;

    @ModelAttribute("unreadCount")
    public long unreadCount() {
        try {
            return notificationService.countUnread();
        } catch (Exception e) {
            return 0L;
        }
    }
    @ModelAttribute("currentUsername")
public String currentUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
        return auth.getName();
    }
    return "Admin";
}
}
