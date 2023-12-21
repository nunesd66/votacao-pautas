package com.nunesd66.votacaopautas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nunesd66.votacaopautas.enumeration.SimNaoEnum;
import com.nunesd66.votacaopautas.model.Associado;
import com.nunesd66.votacaopautas.model.Pauta;
import com.nunesd66.votacaopautas.model.Votacao;
import com.nunesd66.votacaopautas.repository.PautaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class IntegracaoTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PautaRepository pautaRepository;

    @Test
    public void criarVoto_Sucesso() throws Exception {
        Associado associado = new Associado();
        associado.setNome("Nome Associado Teste");
        associado.setCpf("12345678901");

        Pauta pauta = new Pauta();
        pauta.setTitulo("Título teste");
        pauta.setResumo("Resumo teste");
        pauta.setDescricao("Descrição teste");
        pauta.setAssociado(associado);

        MvcResult resultPauta = mvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pauta)))
                .andExpect(status().isCreated())
                .andReturn();

        Pauta responsePauta = objectMapper.readValue(resultPauta.getResponse().getContentAsByteArray(), Pauta.class);

        assertNotNull(responsePauta);
        assertEquals("Título teste", responsePauta.getTitulo());
        assertEquals("Resumo teste", responsePauta.getResumo());
        assertEquals("Descrição teste", responsePauta.getDescricao());

        assertNotNull(responsePauta.getAssociado());
        assertEquals("Nome Associado Teste", responsePauta.getAssociado().getNome());
        assertEquals("12345678901", responsePauta.getAssociado().getCpf());

        Votacao votacao = new Votacao();
        votacao.setPauta(responsePauta);
        votacao.setAssociado(responsePauta.getAssociado());
        votacao.setVoto(SimNaoEnum.SIM);

        MvcResult resultVoto = mvc.perform(post("/votacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votacao)))
                .andExpect(status().isCreated())
                .andReturn();

        Votacao responseVoto = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(), Votacao.class);

        assertNotNull(responseVoto);
        assertEquals(responsePauta.getId(), responseVoto.getPauta().getId());
        assertEquals(responsePauta.getAssociado().getId(), responseVoto.getAssociado().getId());
        assertEquals(SimNaoEnum.SIM, responseVoto.getVoto());
    }

    @Test
    public void criarVoto_Error_AssociadoRepetido() throws Exception {

    }
}
