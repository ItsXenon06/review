package com.abc.backend.CNPM.repository.PhanQuyen;

import com.abc.backend.CNPM.model.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByEmail(String email);
    Page<NguoiDung> findByVaiTroId(Long vaiTroId, Pageable pageable);
    int countByVaiTroId(Long vaiTroId);
    boolean existsByEmail(String email);
}