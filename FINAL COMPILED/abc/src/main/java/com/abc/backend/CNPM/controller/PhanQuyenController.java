package com.abc.backend.CNPM.controller;


import com.abc.backend.CNPM.dto.request.PhanQuyenRq;
import com.abc.backend.CNPM.dto.response.PhanQuyenRp;
import com.abc.backend.CNPM.service.PhanQuyenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phan-quyen")
@RequiredArgsConstructor
public class PhanQuyenController {

    private final PhanQuyenService phanQuyenService;

    // ===================== VAI TRÒ =====================

    /**
     * GET /api/phan-quyen/vai-tro
     * Lấy danh sách tất cả vai trò
     */
    @GetMapping("/vai-tro")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'XEM')")
    public ResponseEntity<PhanQuyenRp> layDanhSachVaiTro(
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "20") int kichThuoc,
            @RequestParam(defaultValue = "id") String sapXepTheo,
            @RequestParam(defaultValue = "asc") String huong) {

        Sort sort = huong.equalsIgnoreCase("desc")
                ? Sort.by(sapXepTheo).descending()
                : Sort.by(sapXepTheo).ascending();
        Pageable pageable = PageRequest.of(trang, kichThuoc, sort);

        return ResponseEntity.ok(phanQuyenService.layDanhSachVaiTro(pageable));
    }

    /**
     * GET /api/phan-quyen/vai-tro/{id}
     * Lấy chi tiết vai trò kèm danh sách quyền
     */
    @GetMapping("/vai-tro/{id}")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'XEM')")
    public ResponseEntity<PhanQuyenRp> layChiTietVaiTro(@PathVariable Long id) {
        return ResponseEntity.ok(phanQuyenService.layChiTietVaiTro(id));
    }

    /**
     * POST /api/phan-quyen/vai-tro
     * Tạo vai trò mới
     */
    @PostMapping("/vai-tro")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'THEM')")
    public ResponseEntity<PhanQuyenRp> taoVaiTro(@Valid @RequestBody PhanQuyenRq request) {
        PhanQuyenRp response = phanQuyenService.taoVaiTro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/phan-quyen/vai-tro/{id}
     * Cập nhật thông tin vai trò
     */
    @PutMapping("/vai-tro/{id}")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'SUA')")
    public ResponseEntity<PhanQuyenRp> capNhatVaiTro(
            @PathVariable Long id,
            @Valid @RequestBody PhanQuyenRq request) {
        return ResponseEntity.ok(phanQuyenService.capNhatVaiTro(id, request));
    }

    /**
     * DELETE /api/phan-quyen/vai-tro/{id}
     * Xoá vai trò
     */
    @DeleteMapping("/vai-tro/{id}")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'XOA')")
    public ResponseEntity<PhanQuyenRp> xoaVaiTro(@PathVariable Long id) {
        return ResponseEntity.ok(phanQuyenService.xoaVaiTro(id));
    }

    // ===================== QUYỀN =====================

    /**
     * PUT /api/phan-quyen/vai-tro/{id}/quyen
     * Lưu toàn bộ quyền cho một vai trò (Lưu thay đổi)
     */
    @PutMapping("/vai-tro/{id}/quyen")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'PHAN_QUYEN')")
    public ResponseEntity<PhanQuyenRp> luuQuyenVaiTro(
            @PathVariable Long id,
            @RequestBody PhanQuyenRq request) {
        return ResponseEntity.ok(phanQuyenService.luuQuyenVaiTro(id, request));
    }

    /**
     * POST /api/phan-quyen/vai-tro/{id}/dat-lai
     * Đặt lại quyền về mặc định
     */
    @PostMapping("/vai-tro/{id}/dat-lai")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'PHAN_QUYEN')")
    public ResponseEntity<PhanQuyenRp> datLaiQuyen(@PathVariable Long id) {
        return ResponseEntity.ok(phanQuyenService.datLaiQuyen(id));
    }

    /**
     * POST /api/phan-quyen/nhap-quyen
     * Nhập quyền từ vai trò khác
     * Body: { vaiTroNguonId: 1, vaiTroDoId: 2 }
     */
    @PostMapping("/nhap-quyen")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'PHAN_QUYEN')")
    public ResponseEntity<PhanQuyenRp> nhapQuyenTuVaiTroKhac(@RequestBody PhanQuyenRq request) {
        return ResponseEntity.ok(phanQuyenService.nhapQuyenTuVaiTroKhac(request));
    }

    // ===================== NGƯỜI DÙNG =====================

    /**
     * GET /api/phan-quyen/vai-tro/{id}/nguoi-dung
     * Lấy danh sách người dùng thuộc vai trò
     */
    @GetMapping("/vai-tro/{id}/nguoi-dung")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'XEM')")
    public ResponseEntity<PhanQuyenRp> layNguoiDungTheoVaiTro(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int trang,
            @RequestParam(defaultValue = "10") int kichThuoc) {

        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("hoTen").ascending());
        return ResponseEntity.ok(phanQuyenService.layNguoiDungTheoVaiTro(id, pageable));
    }

    /**
     * POST /api/phan-quyen/gan-vai-tro
     * Gán vai trò cho người dùng
     * Body: { nguoiDungId: 5, vaiTroId: 2 }
     */
    @PostMapping("/gan-vai-tro")
    @PreAuthorize("hasPermission('PHAN_QUYEN', 'PHAN_QUYEN')")
    public ResponseEntity<PhanQuyenRp> ganVaiTroChoNguoiDung(@RequestBody PhanQuyenRq request) {
        return ResponseEntity.ok(phanQuyenService.ganVaiTroChoNguoiDung(request));
    }

    /**
     * GET /api/phan-quyen/kiem-tra
     * Kiểm tra quyền của người dùng hiện tại
     * Params: nguoiDungId, danhMucChucNang, loaiQuyen
     */
    @GetMapping("/kiem-tra")
    public ResponseEntity<Boolean> kiemTraQuyen(
            @RequestParam Long nguoiDungId,
            @RequestParam String danhMucChucNang,
            @RequestParam String loaiQuyen) {

        boolean coQuyen = phanQuyenService.kiemTraQuyen(nguoiDungId, danhMucChucNang, loaiQuyen);
        return ResponseEntity.ok(coQuyen);
    }
}