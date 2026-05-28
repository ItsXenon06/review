package com.abc.backend.CNPM.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhanQuyenRq {

    // ========== Tạo / Cập nhật Vai Trò ==========

    @NotBlank(message = "Tên vai trò không được để trống")
    private String tenVaiTro;

    private String moTa;

    /**
     * Map danh mục chức năng -> Set các quyền được phép
     * Ví dụ: { "TONG_QUAN": ["XEM", "THEM", "SUA", "XOA", "XUAT_DU_LIEU", "PHAN_QUYEN"] }
     */
    private Map<String, Set<String>> danhSachQuyen;

    // ========== Gán quyền cho người dùng ==========

    private Long nguoiDungId;

    private Long vaiTroId;

    // ========== Nhập quyền từ vai trò khác ==========

    private Long vaiTroNguonId;   // vai trò nguồn để nhập quyền
    private Long vaiTroDoId;      // vai trò đích nhận quyền

    // ========== Cập nhật quyền cụ thể ==========

    /**
     * Danh mục chức năng cần cập nhật quyền
     * Ví dụ: "TONG_QUAN", "LUU_TRU", "HOP_DONG", ...
     */
    private String danhMucChucNang;

    private Boolean xem;
    private Boolean them;
    private Boolean sua;
    private Boolean xoa;
    private Boolean xuatDuLieu;
    private Boolean phanQuyen;
}