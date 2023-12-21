package com.nunesd66.votacaopautas.repository;

import com.nunesd66.votacaopautas.model.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, Long>  {

    @Query("select v from Votacao v " +
            "where v.pauta.id = :idPauta " +
            "and v.associado.id = :idAssociado")
    Optional<Votacao> findByAssociandoAndPauta(Long idPauta, Long idAssociado);

}
