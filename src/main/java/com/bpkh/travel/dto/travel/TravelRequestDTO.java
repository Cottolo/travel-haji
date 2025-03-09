package com.bpkh.travel.dto.travel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelRequestDTO {
    @NotBlank(message = "Nama Travel tidak boleh kosong")
    private String namaTravel;

    @NotBlank(message = "Nomor Telepon tidak boleh kosong")
    @Pattern(regexp = "^0\\d{9,14}$", message = "Nomor Telepon harus berupa angka, diawali 0, dan memiliki 10-15 digit")
    private String noTelp;

    @NotBlank(message = "Alamat tidak boleh kosong")
    private String alamat;

    @NotBlank(message = "Nomor Polisi tidak boleh kosong")
    private String noPolisi;

    @NotBlank(message = "Jenis Bus tidak boleh kosong")
    private String jenisBus;
}
