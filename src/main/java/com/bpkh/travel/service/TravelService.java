package com.bpkh.travel.service;

import com.bpkh.travel.dto.Tiket.TiketResponseDTO;
import com.bpkh.travel.dto.travel.TravelRequestDTO;
import com.bpkh.travel.dto.travel.TravelResponseDTO;
import com.bpkh.travel.exception.DataNotFoundException;
import com.bpkh.travel.exception.DuplicateNoPolisiException;
import com.bpkh.travel.exception.DuplicateTravelNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bpkh.travel.entity.Travel;
import com.bpkh.travel.repository.TravelRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TiketService tiketService;

    public TravelResponseDTO createTravel(TravelRequestDTO request) {
        try {
            String noPolisi = request.getNoPolisi();
            String regex = "[,\\.\\s]";
            String[] myArray = noPolisi.split(regex);
            boolean isValid = false;
            int i = 0;
            for (String s : myArray) {
                if(i==0){
                    if (s.length()>2){
                        throw new RuntimeException("Format No Polisi tidak sesuai.");
                    }
                    try{
                        Integer.parseInt(s);
                        isValid = false;
                    }catch (Exception e){
                        isValid = true;
                    }
                }
                if(i==1){
                    try{
                        Integer.parseInt(s);
                        isValid = true;
                    }catch (Exception e){
                        isValid = false;
                    }
                }
                if(i==2){
                    try{
                        Integer.parseInt(s);
                        isValid = false;
                    }catch (Exception e){
                        isValid = true;
                    }
                }
                i++;
                if (!isValid){
                    throw new RuntimeException("Format No Polisi tidak sesuai.");
                }
            }

            if (!isValid || i<2){
                throw new RuntimeException("Format No Polisi tidak sesuai.");
            }

            Travel travel = new Travel(null, request.getNamaTravel(), request.getNoTelp(),
                request.getAlamat(), request.getNoPolisi(), request.getJenisBus(),null);
            if (travelRepository.existsByNamaTravel(request.getNamaTravel())) {
                throw new DuplicateTravelNameException("Nama Travel " + request.getNamaTravel() + " sudah digunakan!");
            }

            if (travelRepository.existsByNoPolisi(request.getNoPolisi())) {
                throw new DuplicateNoPolisiException("No Polisi " + request.getNoPolisi() + " sudah digunakan!");
            }

            travel = travelRepository.save(travel);
            return mapToResponse(travel);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Page<TravelResponseDTO> getAllTravels(Pageable pageable) {
        try {
            return travelRepository.findAll(pageable).map(this::mapToResponse);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public TravelResponseDTO getTravelById(Long id) {
        try {
            Travel travel = travelRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Travel dengan ID " + id + " tidak ditemukan"));
            return mapToResponse(travel);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public TravelResponseDTO updateTravel(Long id, TravelRequestDTO request) {
        try {
            Travel travel = travelRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Travel dengan ID " + id + " tidak ditemukan"));
            travel.setNamaTravel(request.getNamaTravel());
            travel.setNoTelp(request.getNoTelp());
            travel.setAlamat(request.getAlamat());
            travel.setNoPolisi(request.getNoPolisi());
            travel.setJenisBus(request.getJenisBus());
            return mapToResponse(travelRepository.save(travel));
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteTravel(Long id) {
        try {
            Travel travel = travelRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Travel dengan ID " + id + " tidak ditemukan"));
            travelRepository.delete(travel);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<TravelResponseDTO> getTravelsInJakarta() {
        try {
//        return travelRepository.findByAlamatContainingIgnoreCase("Jakarta")
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
            return travelRepository.findTravelJakarta("%jakarta%")
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public TravelResponseDTO mapToResponse(Travel travel) {
        try {
            List<TiketResponseDTO> tiketList = null;
            if(travel.getTikets() != null){
                tiketList = travel.getTikets()
                        .stream()
                        .map(tiketService::mapToResponse)
                        .collect(Collectors.toList());
            }
            return new TravelResponseDTO(
                    travel.getId(),
                    travel.getNamaTravel(),
                    travel.getNoTelp(),
                    travel.getAlamat(),
                    travel.getNoPolisi(),
                    travel.getJenisBus(),
                    (tiketList == null || tiketList.isEmpty()) ? null:tiketList
            );
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public List<TravelResponseDTO> filterTravels(String namaTravel, String noTelp, String alamat, String noPolisi, String jenisBus) {
        try {
            Specification<Travel> spec = filterBy(namaTravel, noTelp, alamat, noPolisi, jenisBus);
            return travelRepository.findAll(spec)
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private Specification<Travel> filterBy(String namaTravel, String noTelp, String alamat, String noPolisi, String jenisBus) {
        return (root, query, criteriaBuilder) -> {
            Specification<Travel> spec = Specification.where(null);

            if (namaTravel != null) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("namaTravel")), "%" + namaTravel.toLowerCase() + "%"));
            }
            if (noTelp != null) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("noTelp")), "%" + noTelp.toLowerCase() + "%"));
            }
            if (alamat != null) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("alamat")), "%" + alamat.toLowerCase() + "%"));
            }
            if (noPolisi != null) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("noPolisi")), "%" + noPolisi.toLowerCase() + "%"));
            }
            if (jenisBus != null) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(criteriaBuilder1.lower(root1.get("jenisBus")), "%" + jenisBus.toLowerCase() + "%"));
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
