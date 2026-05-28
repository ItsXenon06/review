package com.abc.backend.CNPM.controller;

import com.abc.backend.CNPM.model.PhanQuyen.NguoiDung;
import com.abc.backend.CNPM.repository.PhanQuyen.NguoiDungRepository;
import com.abc.backend.CNPM.security.PhanQuyenSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            body.get("email"), body.get("password")));

            NguoiDung nd = nguoiDungRepository.findByEmail(body.get("email")).orElseThrow();
            String role  = nd.getVaiTro() != null ? nd.getVaiTro().getTenVaiTro() : "KHACH_HANG";
            String token = phanQuyenSecurity.taoToken(nd.getId(), nd.getEmail(), role);

            return ResponseEntity.ok(Map.of(
                    "token",  token,
                    "email",  nd.getEmail(),
                    "hoTen",  nd.getHoTen(),
                    "vaiTro", role));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Email hoặc mật khẩu không đúng"));
        }
    }
}
