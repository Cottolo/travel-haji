package com.bpkh.travel.controller;
import com.bpkh.travel.dto.travel.TravelRequestDTO;
import com.bpkh.travel.dto.travel.TravelResponseDTO;
import com.bpkh.travel.service.TravelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/travels")
public class TravelController {
    @Autowired
    private TravelService travelService;

    @PostMapping
    public ResponseEntity<TravelResponseDTO> createTravel(@Valid @RequestBody TravelRequestDTO request) {
        return ResponseEntity.ok(travelService.createTravel(request));
    }

    @GetMapping
    public Page<TravelResponseDTO> getAllTravels(Pageable pageable) {
        return travelService.getAllTravels(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> getTravelById(@PathVariable Long id) {
        return ResponseEntity.ok(travelService.getTravelById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> updateTravel(@PathVariable Long id, @Valid @RequestBody TravelRequestDTO request) {
        return ResponseEntity.ok(travelService.updateTravel(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTravel(@PathVariable Long id) {
        travelService.deleteTravel(id);
        return ResponseEntity.ok("Travel dengan ID " + id + " berhasil dihapus.");
    }

    @GetMapping("/jakarta")
    public ResponseEntity<List<TravelResponseDTO>> getTravelsInJakarta() {
        return ResponseEntity.ok(travelService.getTravelsInJakarta());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TravelResponseDTO>> filterTravels(
            @RequestParam(required = false) String namaTravel,
            @RequestParam(required = false) String noTelp,
            @RequestParam(required = false) String alamat,
            @RequestParam(required = false) String noPolisi,
            @RequestParam(required = false) String jenisBus
    ) {
        return ResponseEntity.ok(travelService.filterTravels(namaTravel, noTelp, alamat, noPolisi, jenisBus));
    }
}
