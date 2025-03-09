package com.bpkh.travel.service;

import com.bpkh.travel.dto.Tiket.TiketRequestDTO;
import com.bpkh.travel.dto.Tiket.TiketResponseDTO;
import com.bpkh.travel.entity.Penumpang;
import com.bpkh.travel.entity.Tiket;
import com.bpkh.travel.entity.Travel;
import com.bpkh.travel.exception.DataNotFoundException;
import com.bpkh.travel.repository.PenumpangRepository;
import com.bpkh.travel.repository.TiketRepository;
import com.bpkh.travel.repository.TravelRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TiketService {

    @Autowired
    private TiketRepository tiketRepository;

    @Autowired
    private PenumpangRepository penumpangRepository;

    @Autowired
    private TravelRepository travelRepository;

    public TiketResponseDTO createTiket(TiketRequestDTO request){
        try{
            Penumpang penumpang = penumpangRepository.findById(request.getIdPenumpang())
                    .orElseThrow(() -> new DataNotFoundException("Penumpang dengan ID " + request.getIdPenumpang() + " tidak ditemukan"));

            Travel travel = travelRepository.findById(request.getIdTravel())
                    .orElseThrow(() -> new DataNotFoundException("Travel dengan ID " + request.getIdTravel() + " tidak ditemukan"));

            Tiket tiket = new Tiket(penumpang, travel, request.getJadwal());
            tiket = tiketRepository.save(tiket);
            return mapToResponse(tiket);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Page<TiketResponseDTO> getAllTikets(Pageable pageable) {
        try{
            return tiketRepository.findAll(pageable).map(this::mapToResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public TiketResponseDTO getTiketById(Long id) {
        try{
            Tiket tiket = tiketRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Tiket dengan ID " + id + " tidak ditemukan"));
            return mapToResponse(tiket);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public TiketResponseDTO updateTiket(Long id, TiketRequestDTO request) {
        try{
            Tiket tiket = tiketRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Tiket dengan ID " + id + " tidak ditemukan"));

            Penumpang penumpang = penumpangRepository.findById(request.getIdPenumpang())
                    .orElseThrow(() -> new DataNotFoundException("Penumpang dengan ID " + request.getIdPenumpang() + " tidak ditemukan"));

            Travel travel = travelRepository.findById(request.getIdTravel())
                    .orElseThrow(() -> new DataNotFoundException("Travel dengan ID " + request.getIdTravel() + " tidak ditemukan"));

            tiket.setIdPenumpang(request.getIdPenumpang());
            tiket.setPenumpang(penumpang);
            tiket.setTravel(travel);
            return mapToResponse(tiketRepository.save(tiket));
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteTiket(Long id) {
        try{
            Tiket tiket = tiketRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Tiket dengan ID " + id + " tidak ditemukan"));
            tiketRepository.delete(tiket);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public TiketResponseDTO mapToResponse(Tiket tiket) {
        try{
            return new TiketResponseDTO(
                    tiket.getId(),
                    tiket.getPenumpang().getIdPenumpang(),
                    tiket.getTravel().getId(),
                    tiket.getJadwal()
            );
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<TiketResponseDTO> filterTikets(String idPenumpang, String idTravel, String jadwal) {
        try{
            Specification<Tiket> spec = filterBy(idPenumpang, idTravel, jadwal);
            return tiketRepository.findAll(spec)
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private Specification<Tiket> filterBy(String idPenumpang, String idTravel, String jadwal) {
        try{
            return (root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();

                if (idPenumpang != null && !idPenumpang.isEmpty()) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.function(
                                            "TO_CHAR", String.class, root.get("idPenumpang"),
                                            criteriaBuilder.literal("9999999999")
                                    ),
                                    "%" + idPenumpang + "%"
                            ));
                }

                if (idTravel != null && !idTravel.isEmpty()) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.function(
                                            "TO_CHAR", String.class, root.get("idTravel"),
                                            criteriaBuilder.literal("9999999999")
                                    ),
                                    "%" + idTravel + "%"
                            ));
                }

                if (jadwal != null && !jadwal.isEmpty()) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(
                                    criteriaBuilder.function("TO_CHAR", String.class, root.get("jadwal"),
                                            criteriaBuilder.literal("YYYY-MM-DD HH24:MI")),
                                    "%" + jadwal + "%"
                            ));
                }

                return predicate;
            };
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
