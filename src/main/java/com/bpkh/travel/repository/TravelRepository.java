package com.bpkh.travel.repository;

import com.bpkh.travel.entity.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long>, JpaSpecificationExecutor<Travel> {
    Page<Travel> findAll(Pageable pageable);
    List<Travel> findByAlamatContainingIgnoreCase(String alamat);
    @Query("select a from Travel a where lower(a.alamat) like lower(concat('%',:alamat,'%')) ")
    List<Travel> findTravelJakarta(@Param("alamat") String alamat);
    boolean existsByNamaTravel(String namaTravel);
    boolean existsByNoPolisi(String noPolisi);
}
