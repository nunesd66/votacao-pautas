package com.nunesd66.votacaopautas.controller;

import com.nunesd66.votacaopautas.dto.VotacaoDTO;
import com.nunesd66.votacaopautas.entity.Votacao;
import com.nunesd66.votacaopautas.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votacoes")
public class VotacaoController {

    @Autowired
    private VotacaoService votacaoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Votacao>> getAll() {
        return new ResponseEntity<List<Votacao>>(votacaoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/votos-by-pauta/{idPauta}")
    @ResponseBody
    public ResponseEntity<VotacaoDTO> getVotosByPauta(@PathVariable Long idPauta) {
        return new ResponseEntity<VotacaoDTO>(votacaoService.getVotosByPauta(idPauta), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Votacao> create(@RequestBody Votacao votacao) throws Exception {
        return new ResponseEntity<Votacao>(votacaoService.create(votacao), HttpStatus.CREATED);
    }

    @PostMapping("/save-all")
    @ResponseBody
    public ResponseEntity<List<Votacao>> createAll(@RequestBody List<Votacao> votacoes) throws Exception {
        return new ResponseEntity<List<Votacao>>(votacaoService.createAll(votacoes), HttpStatus.CREATED);
    }

}
