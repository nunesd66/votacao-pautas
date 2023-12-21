package com.nunesd66.votacaopautas.service;

import com.nunesd66.votacaopautas.exception.RegraDeNegocioException;
import com.nunesd66.votacaopautas.model.Votacao;
import com.nunesd66.votacaopautas.repository.VotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotacaoService {

    @Autowired
    private VotacaoRepository votacaoRepository;

    private void validate(Votacao votacao) throws RegraDeNegocioException {
        Optional<Votacao> votacaoConsulta = votacaoRepository.findByAssociandoAndPauta(votacao.getPauta().getId(), votacao.getAssociado().getId());

        if (votacaoConsulta.isPresent()) {
            throw new RegraDeNegocioException("O associado já votou nessa pauta.");
        }
    }

    public Votacao create(Votacao votacao) throws Exception {
        validate(votacao);
        return votacaoRepository.save(votacao);
    }

}
