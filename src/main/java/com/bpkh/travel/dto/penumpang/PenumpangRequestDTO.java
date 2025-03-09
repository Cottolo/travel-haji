package com.bpkh.travel.dto.penumpang;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenumpangRequestDTO {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @NotBlank(message = "Nomot Telepon tidak boleh kosong")
    @Pattern(regexp = "^0\\d{9,14}$", message = "Nomor Telepon harus berupa angka, diawali 0, dan memiliki 10-15 digit")
    private String noTelp;
}
