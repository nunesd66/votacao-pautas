package com.nunesd66.votacaopautas.service;

import com.nunesd66.votacaopautas.dto.VotacaoDTO;
import com.nunesd66.votacaopautas.enumeration.SimNaoEnum;
import com.nunesd66.votacaopautas.exception.RegraDeNegocioException;
import com.nunesd66.votacaopautas.model.Pauta;
import com.nunesd66.votacaopautas.model.Votacao;
import com.nunesd66.votacaopautas.repository.VotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotacaoService {

    @Autowired
    private VotacaoRepository votacaoRepository;

    private void validate(Votacao votacao) throws RegraDeNegocioException {
        Optional<Votacao> votacaoConsulta = votacaoRepository.findByAssociandoAndPauta(votacao.getPauta().getId(), votacao.getAssociado().getId());

        if (votacaoConsulta.isPresent()) {
            String msg = "O associado " + votacao.getAssociado().getNome() + " já votou na pauta " + votacao.getPauta().getTitulo() + ".";
            throw new RegraDeNegocioException(msg);
        }
    }

    public Votacao create(Votacao votacao) throws Exception {
        validate(votacao);
        return votacaoRepository.save(votacao);
    }

    public List<Votacao> createAll(List<Votacao> votacoes) throws Exception {
        for (Votacao votos : votacoes) {
            validate(votos);
        }

        return votacaoRepository.saveAll(votacoes);
    }

    public List<Votacao> getAll() {
        return votacaoRepository.findAll();
    }

    public VotacaoDTO getVotosByPauta(Long idPauta) {
        VotacaoDTO votacaoDTO = new VotacaoDTO();

        List<Votacao> votosByPauta = votacaoRepository.findByPauta(idPauta);

        for (Votacao voto : votosByPauta) {
            if (voto.getVoto().equals(SimNaoEnum.SIM)) {
                votacaoDTO.setVotosSim(votacaoDTO.getVotosSim() + 1);
            } else {
                votacaoDTO.setVotosNao(votacaoDTO.getVotosNao() + 1);
            }
        }

        return votacaoDTO;
    }
}
