package com.bpkh.travel.controller;

import com.bpkh.travel.dto.Tiket.TiketRequestDTO;
import com.bpkh.travel.dto.Tiket.TiketResponseDTO;
import com.bpkh.travel.service.TiketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tikets")
public class TiketController {
    @Autowired
    private TiketService tiketService;

    @PostMapping
    public ResponseEntity<TiketResponseDTO> createTiket(@Valid @RequestBody TiketRequestDTO request) {
        return ResponseEntity.ok(tiketService.createTiket(request));
    }

    @GetMapping
    public Page<TiketResponseDTO> getAllTikets(Pageable pageable) {
        return tiketService.getAllTikets(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiketResponseDTO> getTiketById(@PathVariable Long id) {
        return ResponseEntity.ok(tiketService.getTiketById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TiketResponseDTO> updateTiket(@PathVariable Long id, @Valid @RequestBody TiketRequestDTO request) {
        return ResponseEntity.ok(tiketService.updateTiket(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTiket(@PathVariable Long id) {
        tiketService.deleteTiket(id);
        return ResponseEntity.ok("Tiket dengan ID " + id + " berhasil dihapus.");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TiketResponseDTO>> filterTikets(
            @RequestParam(required = false) String idPenumpang,
            @RequestParam(required = false) String idTravel,
            @RequestParam(required = false) String jadwal
    ) {
        return ResponseEntity.ok(tiketService.filterTikets(idPenumpang, idTravel, jadwal));
    }
}
