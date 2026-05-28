package com.abc.backend.CNPM.service;


import com.abc.backend.CNPM.dto.request.PhanQuyenRq;
import com.abc.backend.CNPM.dto.response.PhanQuyenRp;
import com.abc.backend.CNPM.exception.Exception;
import com.abc.backend.CNPM.model.PhanQuyen.DanhMucChucNang;
import com.abc.backend.CNPM.model.PhanQuyen.NguoiDung;
import com.abc.backend.CNPM.model.PhanQuyen.Quyen;
import com.abc.backend.CNPM.model.PhanQuyen.VaiTro;
import com.abc.backend.CNPM.repository.PhanQuyen.NguoiDungRepository;
import com.abc.backend.CNPM.repository.PhanQuyen.QuyenRepository;
import com.abc.backend.CNPM.repository.PhanQuyen.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhanQuyenService {

    private final VaiTroRepository vaiTroRepository;
    private final QuyenRepository quyenRepository;
    private final NguoiDungRepository nguoiDungRepository;

    // Tên vai trò Admin không thể bị xoá hoặc bị hạn chế quyền
    private static final String VAI_TRO_ADMIN = "Admin";

    // ===================== VAI TRÒ =====================

    /**
     * Lấy danh sách tất cả vai trò (có thể phân trang)
     */
    @Transactional(readOnly = true)
    public PhanQuyenRp layDanhSachVaiTro(Pageable pageable) {
        Page<VaiTro> page = vaiTroRepository.findAll(pageable);

        List<PhanQuyenRp.VaiTroRp> danhSach = page.getContent().stream()
                .map(this::mapVaiTroToRp)
                .collect(Collectors.toList());

        return PhanQuyenRp.builder()
                .danhSachVaiTro(danhSach)
                .thanhCong(true)
                .build();
    }

    /**
     * Lấy chi tiết một vai trò kèm danh sách quyền
     */
    @Transactional(readOnly = true)
    public PhanQuyenRp layChiTietVaiTro(Long vaiTroId) {
        VaiTro vaiTro = timVaiTroTheoId(vaiTroId);

        Map<String, PhanQuyenRp.ChiTietQuyenRp> danhSachQuyen = buildDanhSachQuyenRp(vaiTro);

        return PhanQuyenRp.builder()
                .id(vaiTro.getId())
                .tenVaiTro(vaiTro.getTenVaiTro())
                .moTa(vaiTro.getMoTa())
                .laToanQuyen(vaiTro.getLaToanQuyen())
                .soNguoiDung(nguoiDungRepository.countByVaiTroId(vaiTroId))
                .danhSachQuyen(danhSachQuyen)
                .ngayTao(vaiTro.getNgayTao())
                .ngayCapNhat(vaiTro.getNgayCapNhat())
                .thanhCong(true)
                .build();
    }

    /**
     * Tạo vai trò mới
     */
    @Transactional
    public PhanQuyenRp taoVaiTro(PhanQuyenRq request) {
        // Kiểm tra tên vai trò đã tồn tại chưa
        if (vaiTroRepository.existsByTenVaiTro(request.getTenVaiTro())) {
            throw Exception.daTotnai("Vai trò", "tên", request.getTenVaiTro());
        }

        VaiTro vaiTro = VaiTro.builder()
                .tenVaiTro(request.getTenVaiTro())
                .moTa(request.getMoTa())
                .laToanQuyen(false)
                .build();

        vaiTro = vaiTroRepository.save(vaiTro);

        // Tạo các bản ghi quyền mặc định (tất cả false) cho từng danh mục
        taoQuyenMacDinh(vaiTro);

        // Nếu request có danh sách quyền thì cập nhật luôn
        if (request.getDanhSachQuyen() != null && !request.getDanhSachQuyen().isEmpty()) {
            capNhatQuyenChoVaiTro(vaiTro, request.getDanhSachQuyen());
        }

        log.info("Đã tạo vai trò mới: {}", vaiTro.getTenVaiTro());

        return PhanQuyenRp.builder()
                .id(vaiTro.getId())
                .tenVaiTro(vaiTro.getTenVaiTro())
                .thanhCong(true)
                .thongBao("Tạo vai trò thành công")
                .build();
    }

    /**
     * Cập nhật thông tin vai trò
     */
    @Transactional
    public PhanQuyenRp capNhatVaiTro(Long vaiTroId, PhanQuyenRq request) {
        VaiTro vaiTro = timVaiTroTheoId(vaiTroId);

        // Không cho đổi tên vai trò Admin
        if (VAI_TRO_ADMIN.equalsIgnoreCase(vaiTro.getTenVaiTro())
                && !VAI_TRO_ADMIN.equalsIgnoreCase(request.getTenVaiTro())) {
            throw new Exception("Không thể đổi tên vai trò Admin", HttpStatus.FORBIDDEN);
        }

        // Kiểm tra tên mới đã tồn tại chưa (nếu đổi tên)
        if (!vaiTro.getTenVaiTro().equals(request.getTenVaiTro())
                && vaiTroRepository.existsByTenVaiTro(request.getTenVaiTro())) {
            throw Exception.daTotnai("Vai trò", "tên", request.getTenVaiTro());
        }

        vaiTro.setTenVaiTro(request.getTenVaiTro());
        vaiTro.setMoTa(request.getMoTa());
        vaiTroRepository.save(vaiTro);

        log.info("Đã cập nhật vai trò id={}: {}", vaiTroId, vaiTro.getTenVaiTro());

        return PhanQuyenRp.builder()
                .id(vaiTro.getId())
                .tenVaiTro(vaiTro.getTenVaiTro())
                .thanhCong(true)
                .thongBao("Cập nhật vai trò thành công")
                .build();
    }

    /**
     * Xoá vai trò
     */
    @Transactional
    public PhanQuyenRp xoaVaiTro(Long vaiTroId) {
        VaiTro vaiTro = timVaiTroTheoId(vaiTroId);

        if (VAI_TRO_ADMIN.equalsIgnoreCase(vaiTro.getTenVaiTro())) {
            throw new Exception("Không thể xoá vai trò Admin", HttpStatus.FORBIDDEN);
        }

        int soNguoiDung = nguoiDungRepository.countByVaiTroId(vaiTroId);
        if (soNguoiDung > 0) {
            throw Exception.duLieuKhongHopLe(
                    "Vai trò đang có " + soNguoiDung + " người dùng, không thể xoá");
        }

        quyenRepository.deleteByVaiTroId(vaiTroId);
        vaiTroRepository.delete(vaiTro);

        log.info("Đã xoá vai trò id={}: {}", vaiTroId, vaiTro.getTenVaiTro());

        return PhanQuyenRp.builder()
                .thanhCong(true)
                .thongBao("Xoá vai trò thành công")
                .build();
    }

    // ===================== QUYỀN =====================

    /**
     * Lưu thay đổi toàn bộ quyền cho một vai trò
     */
    @Transactional
    public PhanQuyenRp luuQuyenVaiTro(Long vaiTroId, PhanQuyenRq request) {
        VaiTro vaiTro = timVaiTroTheoId(vaiTroId);

        if (vaiTro.getLaToanQuyen() && request.getDanhSachQuyen() != null) {
            // Admin vẫn có thể tuỳ chỉnh nhưng không thể tắt hoàn toàn
            log.warn("Đang tuỳ chỉnh quyền cho vai trò Admin (id={})", vaiTroId);
        }

        if (request.getDanhSachQuyen() != null) {
            capNhatQuyenChoVaiTro(vaiTro, request.getDanhSachQuyen());
        }

        log.info("Đã lưu quyền cho vai trò id={}", vaiTroId);

        return PhanQuyenRp.builder()
                .thanhCong(true)
                .thongBao("Lưu thay đổi quyền thành công")
                .build();
    }

    /**
     * Đặt lại quyền mặc định cho một vai trò
     */
    @Transactional
    public PhanQuyenRp datLaiQuyen(Long vaiTroId) {
        VaiTro vaiTro = timVaiTroTheoId(vaiTroId);

        quyenRepository.deleteByVaiTroId(vaiTroId);

        if (vaiTro.getLaToanQuyen()) {
            taoQuyenToanQuyen(vaiTro);
        } else {
            taoQuyenMacDinh(vaiTro);
        }

        log.info("Đã đặt lại quyền mặc định cho vai trò id={}", vaiTroId);

        return PhanQuyenRp.builder()
                .thanhCong(true)
                .thongBao("Đặt lại quyền thành công")
                .build();
    }

    /**
     * Nhập quyền từ vai trò khác
     */
    @Transactional
    public PhanQuyenRp nhapQuyenTuVaiTroKhac(PhanQuyenRq request) {
        VaiTro vaiTroNguon = timVaiTroTheoId(request.getVaiTroNguonId());
        VaiTro vaiTroDo = timVaiTroTheoId(request.getVaiTroDoId());

        List<Quyen> quyenNguon = quyenRepository.findByVaiTroId(vaiTroNguon.getId());

        // Xoá quyền cũ của vai trò đích
        quyenRepository.deleteByVaiTroId(vaiTroDo.getId());

        // Sao chép quyền từ nguồn sang đích
        List<Quyen> quyenMoi = quyenNguon.stream()
                .map(q -> Quyen.builder()
                        .vaiTro(vaiTroDo)
                        .danhMucChucNang(q.getDanhMucChucNang())
                        .xem(q.getXem())
                        .them(q.getThem())
                        .sua(q.getSua())
                        .xoa(q.getXoa())
                        .xuatDuLieu(q.getXuatDuLieu())
                        .phanQuyen(q.getPhanQuyen())
                        .build())
                .collect(Collectors.toList());

        quyenRepository.saveAll(quyenMoi);

        log.info("Đã nhập quyền từ vai trò '{}' sang '{}'",
                vaiTroNguon.getTenVaiTro(), vaiTroDo.getTenVaiTro());

        return PhanQuyenRp.builder()
                .thanhCong(true)
                .thongBao("Nhập quyền từ vai trò '" + vaiTroNguon.getTenVaiTro() + "' thành công")
                .build();
    }

    // ===================== NGƯỜI DÙNG - VAI TRÒ =====================

    /**
     * Lấy danh sách người dùng theo vai trò
     */
    @Transactional(readOnly = true)
    public PhanQuyenRp layNguoiDungTheoVaiTro(Long vaiTroId, Pageable pageable) {
        timVaiTroTheoId(vaiTroId); // kiểm tra tồn tại

        Page<NguoiDung> page = nguoiDungRepository.findByVaiTroId(vaiTroId, pageable);

        List<PhanQuyenRp.NguoiDungVaiTroRp> danhSach = page.getContent().stream()
                .map(this::mapNguoiDungToRp)
                .collect(Collectors.toList());

        return PhanQuyenRp.builder()
                .danhSachNguoiDung(danhSach)
                .thanhCong(true)
                .build();
    }

    /**
     * Gán vai trò cho người dùng
     */
    @Transactional
    public PhanQuyenRp ganVaiTroChoNguoiDung(PhanQuyenRq request) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(request.getNguoiDungId())
                .orElseThrow(() -> Exception.khongTimThay("Người dùng", request.getNguoiDungId()));

        VaiTro vaiTro = timVaiTroTheoId(request.getVaiTroId());

        nguoiDung.setVaiTro(vaiTro);
        nguoiDungRepository.save(nguoiDung);

        log.info("Đã gán vai trò '{}' cho người dùng id={}", vaiTro.getTenVaiTro(), nguoiDung.getId());

        return PhanQuyenRp.builder()
                .thanhCong(true)
                .thongBao("Gán vai trò thành công")
                .build();
    }

    /**
     * Kiểm tra người dùng có quyền thực hiện chức năng không
     */
    @Transactional(readOnly = true)
    public boolean kiemTraQuyen(Long nguoiDungId, String danhMucChucNang, String loaiQuyen) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> Exception.khongTimThay("Người dùng", nguoiDungId));

        if (nguoiDung.getVaiTro() == null) return false;
        if (nguoiDung.getVaiTro().getLaToanQuyen()) return true;

        return quyenRepository
                .findByVaiTroIdAndDanhMucChucNang(nguoiDung.getVaiTro().getId(), danhMucChucNang)
                .map(q -> switch (loaiQuyen.toUpperCase()) {
                    case "XEM" -> Boolean.TRUE.equals(q.getXem());
                    case "THEM" -> Boolean.TRUE.equals(q.getThem());
                    case "SUA" -> Boolean.TRUE.equals(q.getSua());
                    case "XOA" -> Boolean.TRUE.equals(q.getXoa());
                    case "XUAT_DU_LIEU" -> Boolean.TRUE.equals(q.getXuatDuLieu());
                    case "PHAN_QUYEN" -> Boolean.TRUE.equals(q.getPhanQuyen());
                    default -> false;
                })
                .orElse(false);
    }

    // ===================== PRIVATE HELPERS =====================

    private VaiTro timVaiTroTheoId(Long id) {
        return vaiTroRepository.findById(id)
                .orElseThrow(() -> Exception.khongTimThay("Vai trò", id));
    }

    private void taoQuyenMacDinh(VaiTro vaiTro) {
        List<Quyen> danhSachQuyen = Arrays.stream(DanhMucChucNang.values())
                .map(dmcn -> Quyen.builder()
                        .vaiTro(vaiTro)
                        .danhMucChucNang(dmcn.name())
                        .xem(false)
                        .them(false)
                        .sua(false)
                        .xoa(false)
                        .xuatDuLieu(false)
                        .phanQuyen(false)
                        .build())
                .collect(Collectors.toList());
        quyenRepository.saveAll(danhSachQuyen);
    }

    private void taoQuyenToanQuyen(VaiTro vaiTro) {
        List<Quyen> danhSachQuyen = Arrays.stream(DanhMucChucNang.values())
                .map(dmcn -> Quyen.builder()
                        .vaiTro(vaiTro)
                        .danhMucChucNang(dmcn.name())
                        .xem(true)
                        .them(true)
                        .sua(true)
                        .xoa(true)
                        .xuatDuLieu(true)
                        .phanQuyen(true)
                        .build())
                .collect(Collectors.toList());
        quyenRepository.saveAll(danhSachQuyen);
    }

    private void capNhatQuyenChoVaiTro(VaiTro vaiTro, Map<String, Set<String>> danhSachQuyen) {
        danhSachQuyen.forEach((danhMuc, quyenSet) -> {
            Quyen quyen = quyenRepository
                    .findByVaiTroIdAndDanhMucChucNang(vaiTro.getId(), danhMuc)
                    .orElse(Quyen.builder().vaiTro(vaiTro).danhMucChucNang(danhMuc).build());

            quyen.setXem(quyenSet.contains("XEM"));
            quyen.setThem(quyenSet.contains("THEM"));
            quyen.setSua(quyenSet.contains("SUA"));
            quyen.setXoa(quyenSet.contains("XOA"));
            quyen.setXuatDuLieu(quyenSet.contains("XUAT_DU_LIEU"));
            quyen.setPhanQuyen(quyenSet.contains("PHAN_QUYEN"));

            quyenRepository.save(quyen);
        });
    }

    private Map<String, PhanQuyenRp.ChiTietQuyenRp> buildDanhSachQuyenRp(VaiTro vaiTro) {
        List<Quyen> danhSachQuyen = quyenRepository.findByVaiTroId(vaiTro.getId());
        Map<String, PhanQuyenRp.ChiTietQuyenRp> map = new LinkedHashMap<>();

        for (DanhMucChucNang dmcn : DanhMucChucNang.values()) {
            Quyen quyen = danhSachQuyen.stream()
                    .filter(q -> q.getDanhMucChucNang().equals(dmcn.name()))
                    .findFirst()
                    .orElse(null);

            boolean toanQuyen = Boolean.TRUE.equals(vaiTro.getLaToanQuyen());

            map.put(dmcn.name(), PhanQuyenRp.ChiTietQuyenRp.builder()
                    .danhMucChucNang(dmcn.name())
                    .tenChucNang(dmcn.getTenHienThi())
                    .moTa(dmcn.getMoTa())
                    .xem(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getXem())))
                    .them(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getThem())))
                    .sua(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getSua())))
                    .xoa(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getXoa())))
                    .xuatDuLieu(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getXuatDuLieu())))
                    .phanQuyen(toanQuyen || (quyen != null && Boolean.TRUE.equals(quyen.getPhanQuyen())))
                    .build());
        }
        return map;
    }

    private PhanQuyenRp.VaiTroRp mapVaiTroToRp(VaiTro vaiTro) {
        return PhanQuyenRp.VaiTroRp.builder()
                .id(vaiTro.getId())
                .tenVaiTro(vaiTro.getTenVaiTro())
                .moTa(vaiTro.getMoTa())
                .laToanQuyen(vaiTro.getLaToanQuyen())
                .soNguoiDung(nguoiDungRepository.countByVaiTroId(vaiTro.getId()))
                .build();
    }

    private PhanQuyenRp.NguoiDungVaiTroRp mapNguoiDungToRp(NguoiDung nd) {
        return PhanQuyenRp.NguoiDungVaiTroRp.builder()
                .id(nd.getId())
                .hoTen(nd.getHoTen())
                .email(nd.getEmail())
                .soDienThoai(nd.getSoDienThoai())
                .tenVaiTro(nd.getVaiTro() != null ? nd.getVaiTro().getTenVaiTro() : null)
                .trangThai(nd.getTrangThai())
                .ngayTao(nd.getNgayTao())
                .build();
    }
}