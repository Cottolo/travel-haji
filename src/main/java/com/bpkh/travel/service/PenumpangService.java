package com.bpkh.travel.service;

import com.bpkh.travel.dto.Tiket.TiketResponseDTO;
import com.bpkh.travel.dto.penumpang.PenumpangRequestDTO;
import com.bpkh.travel.dto.penumpang.PenumpangResponseDTO;
import com.bpkh.travel.entity.Penumpang;
import com.bpkh.travel.exception.DataNotFoundException;
import com.bpkh.travel.exception.DuplicatePhoneException;
import com.bpkh.travel.repository.PenumpangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PenumpangService {
    @Autowired
    private PenumpangRepository penumpangRepository;
    @Autowired
    private TiketService tiketService;

    public PenumpangResponseDTO createPenumpang(PenumpangRequestDTO request) {
        try {
            Penumpang penumpang = new Penumpang(null, request.getNama(), request.getNoTelp(),null);
            if (penumpangRepository.existsByNoTelp(request.getNoTelp())) {
                throw new DuplicatePhoneException("Nomor Telepon " + request.getNoTelp() + " sudah digunakan!");
            }

            penumpang = penumpangRepository.save(penumpang);
            return mapToResponse(penumpang);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<PenumpangResponseDTO> getAllPenumpangs(Pageable pageable) {
        try {
            return penumpangRepository.findAll(pageable).map(this::mapToResponse);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PenumpangResponseDTO getPenumpangById(Long id) {
        try{
            Penumpang penumpang = penumpangRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Penumpang dengan ID " + id + " tidak ditemukan"));
            return mapToResponse(penumpang);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PenumpangResponseDTO updatePenumpang(Long id, PenumpangRequestDTO request) {
        try{
            Penumpang penumpang = penumpangRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Penumpang dengan ID " + id + " tidak ditemukan"));
            penumpang.setNama(request.getNama());
            penumpang.setNoTelp(request.getNoTelp());
            return mapToResponse(penumpangRepository.save(penumpang));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deletePenumpang(Long id) {
        try{
            Penumpang penumpang = penumpangRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Penumpang dengan ID " + id + " tidak ditemukan"));
            penumpangRepository.delete(penumpang);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PenumpangResponseDTO mapToResponse(Penumpang penumpang) {
        try{
            List<TiketResponseDTO> tiketList = null;
            if(penumpang.getTiket() != null){
                tiketList = penumpang.getTiket()
                        .stream()
                        .map(tiketService::mapToResponse)
                        .collect(Collectors.toList());
            }
            return new PenumpangResponseDTO(
                    penumpang.getIdPenumpang(),
                    penumpang.getNama(),
                    penumpang.getNoTelp(),
                    (tiketList == null || tiketList.isEmpty()) ? null : tiketList
            );
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<PenumpangResponseDTO> filterPenumpangs(String nama, String noTelp) {
        try{
            Specification<Penumpang> spec = filterBy(nama, noTelp);
            return penumpangRepository.findAll(spec)
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Specification<Penumpang> filterBy(String nama, String noTelp) {
        try{
            return (root, query, criteriaBuilder) -> {
                Specification<Penumpang> spec = Specification.where(null);

                if (nama != null) {
                    spec = spec.and((root1, query1, criteriaBuilder1) ->
                            criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("nama")), "%" + nama.toLowerCase() + "%"));
                }
                if (noTelp != null) {
                    spec = spec.and((root1, query1, criteriaBuilder1) ->
                            criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("noTelp")), "%" + noTelp.toLowerCase() + "%"));
                }

                return spec.toPredicate(root, query, criteriaBuilder);
            };
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
