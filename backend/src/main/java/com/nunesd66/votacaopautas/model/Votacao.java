package com.nunesd66.votacaopautas.model;

import com.nunesd66.votacaopautas.enumeration.SimNaoEnum;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "votacao")
public class Votacao {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pauta pauta;

    @ManyToOne(optional = false)
    private Associado associado;

    @Column(length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    private SimNaoEnum voto;

}
