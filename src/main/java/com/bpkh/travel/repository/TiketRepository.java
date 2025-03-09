package com.bpkh.travel.repository;

import com.bpkh.travel.entity.Penumpang;
import com.bpkh.travel.entity.Tiket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TiketRepository extends JpaRepository<Tiket,Long>, JpaSpecificationExecutor<Tiket> {
    Page<Tiket> findAll(Pageable pageable);
}
