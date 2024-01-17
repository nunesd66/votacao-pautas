package com.nunesd66.votacaopautas.controller;

import com.nunesd66.votacaopautas.entity.Pauta;
import com.nunesd66.votacaopautas.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Pauta> create(@RequestBody Pauta pauta) {
        return new ResponseEntity<Pauta>(pautaService.create(pauta), HttpStatus.CREATED);
    }

}
