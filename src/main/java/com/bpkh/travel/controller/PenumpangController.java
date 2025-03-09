package com.bpkh.travel.controller;

import com.bpkh.travel.dto.penumpang.PenumpangRequestDTO;
import com.bpkh.travel.dto.penumpang.PenumpangResponseDTO;
import com.bpkh.travel.service.PenumpangService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/penumpang")
public class PenumpangController {
    @Autowired
    private PenumpangService penumpangService;

    @PostMapping
    public ResponseEntity<PenumpangResponseDTO> createPenumpang(@Valid @RequestBody PenumpangRequestDTO request) {
        return ResponseEntity.ok(penumpangService.createPenumpang(request));
    }

    @GetMapping
    public Page<PenumpangResponseDTO> getAllPenumpangs(Pageable pageable) {
        return penumpangService.getAllPenumpangs(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PenumpangResponseDTO> getPenumpangById(@PathVariable Long id) {
        return ResponseEntity.ok(penumpangService.getPenumpangById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PenumpangResponseDTO> updatePenumpang(@PathVariable Long id, @Valid @RequestBody PenumpangRequestDTO request) {
        return ResponseEntity.ok(penumpangService.updatePenumpang(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePenumpang(@PathVariable Long id) {
        penumpangService.deletePenumpang(id);
        return ResponseEntity.ok("Penumpang dengan ID " + id + " berhasil dihapus.");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PenumpangResponseDTO>> filterPenumpangs(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String noTelp
    ) {
        return ResponseEntity.ok(penumpangService.filterPenumpangs(nama, noTelp));
    }
}
