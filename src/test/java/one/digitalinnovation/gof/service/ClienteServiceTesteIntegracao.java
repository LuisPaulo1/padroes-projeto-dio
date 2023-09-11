package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import one.digitalinnovation.gof.util.ClienteFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ClienteServiceTesteIntegracao {

    @Autowired
    private ClienteService clienteService;

    private Long idExiste;
    private Long idNaoExiste;

    @BeforeEach
    void setUp() throws Exception {
        idExiste = 1L;
        idNaoExiste = 100L;
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void buscarTodosDeveriaRetornarListaDeClientes() {
        List<Cliente> clientes = clienteService.buscarTodos();

        Assertions.assertFalse(clientes.isEmpty());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void buscarPorIdDeveriaRetornarCliente() {
        Cliente cliente = clienteService.buscarPorId(idExiste);

        Assertions.assertNotNull(cliente);
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void buscarPorIdDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {

        Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
            clienteService.buscarPorId(idNaoExiste);
        });
    }

    @Test
    void inserirDeveriaInserirCliente() {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");

        cliente = clienteService.inserir(cliente);

        Assertions.assertNotNull(cliente.getId());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void atualizarDeveriaAtualizarCliente() {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");

        cliente = clienteService.atualizar(idExiste, cliente);

        Assertions.assertEquals(cliente.getNome(), "João da Silva");
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void deletarDeveriaDeletarCliente() {
        clienteService.deletar(idExiste);
        List<Cliente> clientes = clienteService.buscarTodos();

        Assertions.assertTrue(clientes.isEmpty());
    }

}
