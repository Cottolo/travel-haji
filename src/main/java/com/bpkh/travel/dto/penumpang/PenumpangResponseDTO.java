package com.bpkh.travel.dto.penumpang;

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
public class PenumpangResponseDTO {
    private Long id;
    private String nama;
    private String noTelp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TiketResponseDTO> tiket;
}
