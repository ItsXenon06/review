package com.abc.backend.CNPM.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String maLoi;

    public Exception(String thongBao) {
        super(thongBao);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.maLoi = "LOI_HE_THONG";
    }

    public Exception(String thongBao, HttpStatus httpStatus) {
        super(thongBao);
        this.httpStatus = httpStatus;
        this.maLoi = httpStatus.name();
    }

    public Exception(String thongBao, HttpStatus httpStatus, String maLoi) {
        super(thongBao);
        this.httpStatus = httpStatus;
        this.maLoi = maLoi;
    }

    // ========== Factory methods tiện lợi ==========

    public static Exception khongTimThay(String tenDoiTuong, Object id) {
        return new Exception(
                tenDoiTuong + " không tồn tại với id: " + id,
                HttpStatus.NOT_FOUND,
                "KHONG_TIM_THAY"
        );
    }

    public static Exception daTotnai(String tenDoiTuong, String truong, Object giaTri) {
        return new Exception(
                tenDoiTuong + " đã tồn tại với " + truong + ": " + giaTri,
                HttpStatus.CONFLICT,
                "DA_TON_TAI"
        );
    }

    public static Exception khongCoQuyen(String chucNang) {
        return new Exception(
                "Bạn không có quyền thực hiện chức năng: " + chucNang,
                HttpStatus.FORBIDDEN,
                "KHONG_CO_QUYEN"
        );
    }

    public static Exception duLieuKhongHopLe(String moTa) {
        return new Exception(
                "Dữ liệu không hợp lệ: " + moTa,
                HttpStatus.BAD_REQUEST,
                "DU_LIEU_KHONG_HOP_LE"
        );
    }

    public static Exception khongTheBoPhanQuyen() {
        return new Exception(
                "Không thể bỏ phân quyền cho vai trò Admin",
                HttpStatus.FORBIDDEN,
                "KHONG_THE_BO_PHAN_QUYEN_ADMIN"
        );
    }
}