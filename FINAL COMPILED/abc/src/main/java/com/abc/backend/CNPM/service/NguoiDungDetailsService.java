package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.model.NguoiDung;
import com.abc.backend.CNPM.repository.PhanQuyen.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NguoiDungDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy người dùng với email: " + email));

        String roleName = nd.getVaiTro() != null
                ? nd.getVaiTro().getTenVaiTro()
                : "KHACH_HANG";

        return User.builder()
                .username(nd.getEmail())
                .password(nd.getMatKhau())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + roleName)))
                .accountLocked(Boolean.FALSE.equals(nd.getTrangThai()))
                .build();
    }
}