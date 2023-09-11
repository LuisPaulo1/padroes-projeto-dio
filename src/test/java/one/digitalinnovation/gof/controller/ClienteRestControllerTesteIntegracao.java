package one.digitalinnovation.gof.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.util.ClienteFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ClienteRestControllerTesteIntegracao {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long idExiste = 1L;

    private Long idNaoExiste = 100L;

    @Test
    void buscarTodosDeveriaRetornarRecursosComStatusOk() throws Exception {
        ResultActions result = mockMvc.perform(get("/clientes")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }


    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void buscarPorIdDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(get("/clientes/{id}", idExiste)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void buscarPorIdDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(get("/clientes/{id}", idNaoExiste)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isNotFound());
    }

    @Test
    void inserirDeveriaSalvarClienteRetornandoStatusCreated() throws Exception {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");
        String jsonBody = objectMapper.writeValueAsString(cliente);

        ResultActions resultado = mockMvc.perform(post("/clientes")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isCreated());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void atualizarDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");
        String jsonBody = objectMapper.writeValueAsString(cliente);

        ResultActions resultado = mockMvc.perform(put("/clientes/{id}", idExiste)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'João da Costa')")
    @Test
    void deletarDeveriaRetornarStatusNoContentQuandoIdExistir() throws Exception {
        ResultActions resultado = mockMvc.perform(delete("/clientes/{id}", idExiste)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isNoContent());
    }

}
