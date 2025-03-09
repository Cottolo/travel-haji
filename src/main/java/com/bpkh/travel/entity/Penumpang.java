package com.bpkh.travel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema= "bpkh",name="penumpang")
public class Penumpang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penumpang")
    private Long idPenumpang;

    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @Column(name = "no_telp", nullable = false, unique = true, length = 20)
    private String noTelp;

    @OneToMany(mappedBy = "penumpang", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tiket> tiket;
}
