package com.abc.backend.CNPM.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhanQuyenRp {

    private Long    id;
    private String  tenVaiTro;
    private String  moTa;
    private Boolean laToanQuyen;
    private int     soNguoiDung;
    private Map<String, ChiTietQuyenRp> danhSachQuyen;
    private List<VaiTroRp>              danhSachVaiTro;
    private List<NguoiDungVaiTroRp>     danhSachNguoiDung;
    private LocalDateTime               ngayTao;
    private LocalDateTime               ngayCapNhat;
    private boolean                     thanhCong;
    private String                      thongBao;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class VaiTroRp {
        private Long    id;
        private String  tenVaiTro;
        private String  moTa;
        private Boolean laToanQuyen;
        private int     soNguoiDung;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ChiTietQuyenRp {
        private String  danhMucChucNang;
        private String  tenChucNang;
        private String  moTa;
        private boolean xem;
        private boolean them;
        private boolean sua;
        private boolean xoa;
        private boolean xuatDuLieu;
        private boolean phanQuyen;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NguoiDungVaiTroRp {
        private Long          id;
        private String        hoTen;
        private String        email;
        private String        soDienThoai;
        private String        tenVaiTro;
        private Boolean       trangThai;
        private LocalDateTime ngayTao;
    }
}
