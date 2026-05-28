package com.abc.backend.CNPM.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quyen", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"vai_tro_id", "danh_muc_chuc_nang"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "vaiTro")
public class Quyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vai_tro_id", nullable = false)
    private VaiTro vaiTro;

    @Column(name = "danh_muc_chuc_nang", nullable = false, length = 50)
    private String danhMucChucNang;

    @Column(name = "xem")
    @Builder.Default
    private Boolean xem = false;

    @Column(name = "them")
    @Builder.Default
    private Boolean them = false;

    @Column(name = "sua")
    @Builder.Default
    private Boolean sua = false;

    @Column(name = "xoa")
    @Builder.Default
    private Boolean xoa = false;

    @Column(name = "xuat_du_lieu")
    @Builder.Default
    private Boolean xuatDuLieu = false;

    @Column(name = "phan_quyen")
    @Builder.Default
    private Boolean phanQuyen = false;
}