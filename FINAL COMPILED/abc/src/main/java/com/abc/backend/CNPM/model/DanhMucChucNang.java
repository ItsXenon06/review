package com.abc.backend.CNPM.model;


public enum DanhMucChucNang {

    TONG_QUAN("Tổng quan", "Xem tổng quan hệ thống"),
    LUU_TRU("Lưu trữ", "Quản lý dữ liệu xe, khách hàng..."),
    TRA_CUU("Tra cứu", "Tra cứu thông tin"),
    THONG_KE("Thống kê", "Thống kê, báo cáo"),
    TINH_TOAN("Tính toán", "Tính toán chi phí, khấu hao"),
    HOP_DONG("Hợp đồng", "Quản lý hợp đồng thuê xe"),
    KHACH_HANG("Khách hàng", "Quản lý khách hàng"),
    XE("Xe", "Quản lý xe"),
    THANH_TOAN("Thanh toán", "Quản lý thanh toán, hóa đơn"),
    THONG_BAO("Thông báo", "Quản lý thông báo, nhắc nhở"),
    CAI_DAT_HE_THONG("Cài đặt hệ thống", "Cài đặt chung"),
    PHAN_QUYEN("Phân quyền", "Quản lý vai trò, phân quyền");

    private final String tenHienThi;
    private final String moTa;

    DanhMucChucNang(String tenHienThi, String moTa) {
        this.tenHienThi = tenHienThi;
        this.moTa = moTa;
    }

    public String getTenHienThi() { return tenHienThi; }
    public String getMoTa() { return moTa; }
}