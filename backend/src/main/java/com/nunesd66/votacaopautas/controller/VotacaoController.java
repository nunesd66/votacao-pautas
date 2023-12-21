package com.nunesd66.votacaopautas.controller;

import com.nunesd66.votacaopautas.model.Votacao;
import com.nunesd66.votacaopautas.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votacoes")
public class VotacaoController {

    @Autowired
    private VotacaoService votacaoService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Votacao> create(@RequestBody Votacao votacao) throws Exception {
        return new ResponseEntity<Votacao>(votacaoService.create(votacao), HttpStatus.CREATED);
    }

}
