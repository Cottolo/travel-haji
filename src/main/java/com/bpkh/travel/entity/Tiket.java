package com.bpkh.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(schema= "bpkh",name="tiket")
public class Tiket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_penumpang", insertable = false, updatable = false)
    private Long idPenumpang;

    @Column(name = "id_travel", insertable = false, updatable = false)
    private Long idTravel;

    @Column(name = "jadwal", nullable = false)
    private LocalDateTime jadwal;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Penumpang.class)
    @JoinColumn(name = "id_penumpang", referencedColumnName = "id_penumpang", nullable = false)
    private Penumpang penumpang;

    @ManyToOne(targetEntity = Travel.class)
    @JoinColumn(name = "id_travel", referencedColumnName = "id", nullable = false)
    private Travel travel;

    public Tiket(Penumpang penumpang, Travel travel, LocalDateTime jadwal) {
        this.idPenumpang = penumpang.getIdPenumpang();
        this.idTravel = travel.getId();
        this.jadwal = jadwal;
        this.travel = travel;
        this.penumpang = penumpang;
    }
}
