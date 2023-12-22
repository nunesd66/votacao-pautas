package com.nunesd66.votacaopautas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nunesd66.votacaopautas.dto.VotacaoDTO;
import com.nunesd66.votacaopautas.enumeration.SimNaoEnum;
import com.nunesd66.votacaopautas.exception.RegraDeNegocioException;
import com.nunesd66.votacaopautas.model.Associado;
import com.nunesd66.votacaopautas.model.Pauta;
import com.nunesd66.votacaopautas.model.Votacao;
import com.nunesd66.votacaopautas.repository.AssociadoRepository;
import com.nunesd66.votacaopautas.repository.PautaRepository;
import com.nunesd66.votacaopautas.repository.VotacaoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class IntegracaoTest {

    private final static String URL_VOTOS = "/votacoes";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private VotacaoRepository votacaoRepository;


    private Pauta pauta1 = new Pauta();
    private Pauta pauta2 = new Pauta();

    private Associado associado1 = new Associado();
    private Associado associado2 = new Associado();
    private Associado associado3 = new Associado();


    private Votacao votacao1 = new Votacao();
    private Votacao votacao2 = new Votacao();
    private Votacao votacao3 = new Votacao();

    @BeforeEach
    public void initialData() {
        associado1.setNome("Nome Associado Teste 1");
        associado1.setCpf("12345678901");

        associado2.setNome("Nome Associado Teste 2");
        associado2.setCpf("12345678902");

        associado3.setNome("Nome Associado Teste 3");
        associado3.setCpf("12345678903");

        pauta1.setTitulo("Título teste 1");
        pauta1.setResumo("Resumo teste 1");
        pauta1.setDescricao("Descrição teste 1");
        pauta1.setAssociado(associado1);

        pauta2.setTitulo("Título teste 2");
        pauta2.setResumo("Resumo teste 2");
        pauta2.setDescricao("Descrição teste 2");
        pauta2.setAssociado(associado2);

        associado1 = associadoRepository.save(associado1);
        associado2 = associadoRepository.save(associado2);
        associado3 = associadoRepository.save(associado3);

        pauta1 = pautaRepository.save(pauta1);
        pauta2 = pautaRepository.save(pauta2);

        votacao1.setPauta(pauta2);
        votacao1.setAssociado(associado1);
        votacao1.setVoto(SimNaoEnum.SIM);

        votacao2.setPauta(pauta2);
        votacao2.setAssociado(associado2);
        votacao2.setVoto(SimNaoEnum.SIM);

        votacao3.setPauta(pauta2);
        votacao3.setAssociado(associado3);
        votacao3.setVoto(SimNaoEnum.NAO);

        votacao1 = votacaoRepository.save(votacao1);
        votacao2 = votacaoRepository.save(votacao2);
        votacao3 = votacaoRepository.save(votacao3);
    }

    @Test
    public void criarVoto_Sucesso201_UmRegistro() throws Exception {
        Votacao votacao = new Votacao();
        votacao.setPauta(pauta1);
        votacao.setAssociado(associado1);
        votacao.setVoto(SimNaoEnum.SIM);

        MvcResult resultVoto = mvc.perform(post(URL_VOTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votacao)))
                .andExpect(status().isCreated())
                .andReturn();

        Votacao responseVoto = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(), Votacao.class);

        assertNotNull(responseVoto);
        assertEquals(pauta1.getId(), responseVoto.getPauta().getId());
        assertEquals(associado1.getId(), responseVoto.getAssociado().getId());
        assertEquals(SimNaoEnum.SIM, responseVoto.getVoto());
    }

    @Test
    public void criarVoto_Sucesso201_VariosRegistros() throws Exception {
        Votacao votacao1 = new Votacao();
        votacao1.setPauta(pauta1);
        votacao1.setAssociado(associado1);
        votacao1.setVoto(SimNaoEnum.SIM);

        Votacao votacao2 = votacao1;
        votacao2.setAssociado(associado2);

        Votacao votacao3 = votacao1;
        votacao3.setAssociado(associado3);

        List<Votacao> votos = Arrays.asList(votacao1, votacao2, votacao3);

        MvcResult resultVoto = mvc.perform(post(URL_VOTOS + "/save-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votos)))
                .andExpect(status().isCreated())
                .andReturn();
        List<Votacao> responseVotos = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(),
                new TypeReference<List<Votacao>>() {});

        assertEquals(3, responseVotos.size());
        for (Votacao response : responseVotos) {
            assertNotNull(response);
            assertNotNull(response.getPauta());
            assertNotNull(response.getAssociado());
            assertEquals(SimNaoEnum.SIM, response.getVoto());
        }
    }

    @Test
    public void criarVoto_Error400_AssociadoRepetido() throws Exception {
        Votacao votacao = new Votacao();
        votacao.setPauta(pauta1);
        votacao.setAssociado(associado1);
        votacao.setVoto(SimNaoEnum.SIM);

        MvcResult resultVoto = mvc.perform(post(URL_VOTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votacao)))
                .andExpect(status().isCreated())
                .andReturn();

        Votacao responseVoto = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(), Votacao.class);

        assertNotNull(responseVoto);
        assertEquals(pauta1.getId(), responseVoto.getPauta().getId());
        assertEquals(associado1.getId(), responseVoto.getAssociado().getId());
        assertEquals(SimNaoEnum.SIM, responseVoto.getVoto());

        mvc.perform(post(URL_VOTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votacao)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(RegraDeNegocioException.class, result.getResolvedException()));
    }

    @Test
    public void getVotos_Sucesso200_getAll() throws Exception {
        MvcResult resultVoto = mvc.perform(get(URL_VOTOS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Votacao> responseVotos = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(),
                new TypeReference<List<Votacao>>() {});

        assertEquals(3, responseVotos.size());
        for (Votacao response : responseVotos) {
            assertNotNull(response);
            assertNotNull(response.getPauta());
            assertNotNull(response.getAssociado());
            assertNotNull(response.getVoto());
        }
    }

    @Test
    public void getVotos_Sucesso200_getVotosByPauta() throws Exception {
        MvcResult resultVoto = mvc.perform(get(URL_VOTOS + "/votos-by-pauta/" + pauta2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VotacaoDTO votacaoDTO = objectMapper.readValue(resultVoto.getResponse().getContentAsByteArray(), VotacaoDTO.class);

        assertNotNull(votacaoDTO);
        assertEquals(2, votacaoDTO.getVotosSim());
        assertEquals(1, votacaoDTO.getVotosNao());
    }
}
