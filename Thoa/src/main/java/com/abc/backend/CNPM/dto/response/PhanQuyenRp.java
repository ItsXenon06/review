package com.abc.backend.CNPM.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhanQuyenRp {

    // ========== Thông tin Vai Trò ==========

    private Long id;
    private String tenVaiTro;
    private String moTa;
    private Boolean laToanQuyen;        // Admin có toàn quyền hệ thống
    private Integer soNguoiDung;        // Số người dùng thuộc vai trò
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    /**
     * Map: danh mục chức năng -> chi tiết quyền
     * Ví dụ: { "TONG_QUAN": { xem: true, them: true, ... } }
     */
    private Map<String, ChiTietQuyenRp> danhSachQuyen;

    // ========== Danh sách (dùng cho list API) ==========

    private List<VaiTroRp> danhSachVaiTro;
    private List<NguoiDungVaiTroRp> danhSachNguoiDung;

    // ========== Response chung ==========

    private Boolean thanhCong;
    private String thongBao;

    // ========== Inner classes ==========

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChiTietQuyenRp {
        private String danhMucChucNang;
        private String tenChucNang;
        private String moTa;
        private Boolean xem;
        private Boolean them;
        private Boolean sua;
        private Boolean xoa;
        private Boolean xuatDuLieu;
        private Boolean phanQuyen;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VaiTroRp {
        private Long id;
        private String tenVaiTro;
        private String moTa;
        private Boolean laToanQuyen;
        private Integer soNguoiDung;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NguoiDungVaiTroRp {
        private Long id;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private String tenVaiTro;
        private Boolean trangThai;
        private LocalDateTime ngayTao;
    }
}