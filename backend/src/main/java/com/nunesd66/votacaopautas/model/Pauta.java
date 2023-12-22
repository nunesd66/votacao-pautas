package com.nunesd66.votacaopautas.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pauta")
public class Pauta {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String resumo;

    @Lob
    @Column(nullable = false, length = 5000)
    private String descricao;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_pauta_associado"))
    private Associado associado;

    @Column(name = "data_encerramento_votacoes", nullable = false, updatable = false)
    private LocalDate dataEncerramentoVotacoes;

    @PrePersist
    public void aoPersistir() {
        if (dataEncerramentoVotacoes == null) {
            dataEncerramentoVotacoes = LocalDate.now().plusDays(7);
        }
    }
}
