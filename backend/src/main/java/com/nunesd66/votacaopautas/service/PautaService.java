package com.nunesd66.votacaopautas.service;

import com.nunesd66.votacaopautas.entity.Pauta;
import com.nunesd66.votacaopautas.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public Pauta create(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

}
