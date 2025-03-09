package com.bpkh.travel.dto.Tiket;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiketRequestDTO {
    @NotNull(message = "Id Penumpang tidak boleh kosong")
    private Long idPenumpang;

    @NotNull(message = "Id Travel tidak boleh kosong")
    private Long idTravel;

    @NotNull(message = "Jadwal tidak boleh kosong")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime Jadwal;
}
