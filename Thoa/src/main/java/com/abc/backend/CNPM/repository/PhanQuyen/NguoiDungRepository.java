package com.abc.backend.CNPM.repository.PhanQuyen;


import com.abc.backend.CNPM.model.PhanQuyen.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    Optional<NguoiDung> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<NguoiDung> findByVaiTroId(Long vaiTroId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.vaiTro.id = :vaiTroId")
    int countByVaiTroId(@Param("vaiTroId") Long vaiTroId);
}