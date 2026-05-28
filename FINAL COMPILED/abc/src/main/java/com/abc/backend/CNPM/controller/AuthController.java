package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.NguoiDung;
import com.abc.backend.CNPM.repository.PhanQuyen.NguoiDungRepository;
import com.abc.backend.CNPM.security.PhanQuyenSecurity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager  authenticationManager;
    private final PhanQuyenSecurity      phanQuyenSecurity;
    private final NguoiDungRepository    nguoiDungRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> apiLogin(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            log.info("Attempting login for user: {}", email);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            NguoiDung nd = nguoiDungRepository.findByEmail(email).orElseThrow();
            String role  = nd.getVaiTro() != null ? nd.getVaiTro().getTenVaiTro() : "KHACH_HANG";
            String token = phanQuyenSecurity.taoToken(nd.getId(), nd.getEmail(), role);

            log.info("Login successful for user: {}", email);

            return ResponseEntity.ok(Map.of(
                    "token",  token,
                    "email",  nd.getEmail(),
                    "hoTen",  nd.getHoTen(),
                    "vaiTro", role));

        } catch (AuthenticationException ex) {
            log.error("Authentication failed for user {}: {}", body.get("email"), ex.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Email hoặc mật khẩu không đúng"));
        } catch (Exception ex) {
            log.error("An unexpected error occurred during login for user {}: {}", body.get("email"), ex.getMessage(), ex);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }
}