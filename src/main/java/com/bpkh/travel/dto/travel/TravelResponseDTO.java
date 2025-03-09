package com.bpkh.travel.dto.travel;

import com.bpkh.travel.dto.Tiket.TiketResponseDTO;
import com.bpkh.travel.entity.Tiket;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravelResponseDTO {
    private Long id;
    private String namaTravel;
    private String noTelp;
    private String alamat;
    private String noPolisi;
    private String jenisBus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TiketResponseDTO> tiket;
}
