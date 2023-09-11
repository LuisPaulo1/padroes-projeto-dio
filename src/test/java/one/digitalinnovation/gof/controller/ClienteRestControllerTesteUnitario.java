package one.digitalinnovation.gof.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import one.digitalinnovation.gof.util.ClienteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteRestController.class)
public class ClienteRestControllerTesteUnitario {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    private Long idExiste;

    private Long idNaoExiste;

    @BeforeEach
    void setUp() throws Exception {
        idExiste = 1L;
        idNaoExiste = 100L;
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente = ClienteFactory.clienteFactory();

        when(clienteService.buscarTodos()).thenReturn(clientes);

        when(clienteService.buscarPorId(idExiste)).thenReturn(cliente);
        doThrow(EntidadeNaoEncontradaException.class).when(clienteService).buscarPorId(idNaoExiste);

        when(clienteService.inserir(any())).thenReturn(cliente);

        when(clienteService.atualizar(eq(idExiste), any())).thenReturn(cliente);
        doThrow(EntidadeNaoEncontradaException.class).when(clienteService).atualizar(eq(idNaoExiste), any());

        doNothing().when(clienteService).deletar(idExiste);
    }

    @Test
    void buscarTodosDeveriaRetornarRecursosComStatusOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/clientes")
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    void buscarPorIdDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(get("/clientes/{id}", idExiste)
                .contentType(MediaType.APPLICATION_JSON));
        resultado.andExpect(status().isOk());
    }

    @Test
    void buscarPorIdDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(get("/clientes/{id}", idNaoExiste)
                .contentType(MediaType.APPLICATION_JSON));
        resultado.andExpect(status().isNotFound());
    }

    @Test
    void inserirDeveriaSalvarClienteRetornandoStatusCreated() throws Exception {
        Cliente cliente = ClienteFactory.clienteFactory();
        String jsonBody = objectMapper.writeValueAsString(cliente);

        ResultActions resultado = mockMvc.perform(post("/clientes")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isCreated());
    }

    @Test
    void atualizarDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
        Cliente cliente = ClienteFactory.clienteFactory();

        String jsonBody = objectMapper.writeValueAsString(cliente);

        ResultActions resultado = mockMvc.perform(put("/clientes/{id}", idExiste)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
    }

    @Test
    void atualizarDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {
        Cliente cliente = ClienteFactory.clienteFactory();

        String jsonBody = objectMapper.writeValueAsString(cliente);

        ResultActions resultado = mockMvc.perform(put("/clientes/{id}", idNaoExiste)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isNotFound());
    }

    @Test
    void deletarDeveriaRetornarStatusNoContentQuandoIdExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(delete("/clientes/{id}", idExiste)
                .contentType(MediaType.APPLICATION_JSON));
        resultado.andExpect(status().isNoContent());
    }
}
