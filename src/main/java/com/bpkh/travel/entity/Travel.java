package com.bpkh.travel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema= "bpkh",name="travel")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_travel", nullable = false, unique = true, length = 100)
    private String namaTravel;

    @Column(name = "no_telp", nullable = false, length = 20)
    private String noTelp;

    @Column(name = "alamat", nullable = false, columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "no_polisi", nullable = false, unique = true, length = 15)
    private String noPolisi;

    @Column(name = "jenis_bus", nullable = false, length = 50)
    private String jenisBus;

    @OneToMany(mappedBy = "travel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tiket> tikets;
}
