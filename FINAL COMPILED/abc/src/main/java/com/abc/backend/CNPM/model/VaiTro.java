package com.abc.backend.CNPM.model.PhanQuyen;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vai_tro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"danhSachQuyen", "danhSachNguoiDung"})
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_vai_tro", nullable = false, unique = true, length = 100)
    private String tenVaiTro;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    @Column(name = "la_toan_quyen", nullable = false)
    @Builder.Default
    private Boolean laToanQuyen = false;

    @OneToMany(mappedBy = "vaiTro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Quyen> danhSachQuyen = new ArrayList<>();

    @OneToMany(mappedBy = "vaiTro", fetch = FetchType.LAZY)
    @Builder.Default
    private List<NguoiDung> danhSachNguoiDung = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}