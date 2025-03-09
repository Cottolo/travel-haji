package com.bpkh.travel.repository;

import com.bpkh.travel.entity.Penumpang;
import com.bpkh.travel.entity.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PenumpangRepository extends JpaRepository<Penumpang,Long>, JpaSpecificationExecutor<Penumpang> {
    Page<Penumpang> findAll(Pageable pageable);
    boolean existsByNoTelp(String noTelp);
}
