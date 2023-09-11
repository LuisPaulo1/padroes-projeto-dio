package one.digitalinnovation.gof.repository;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.util.ClienteFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
public class ClienteRepositoryTesteUnitario {

    @Autowired
    private ClienteRepository clienteRepository;

    private Long idExiste;
    private Long idNaoExiste;

    @BeforeEach
    void setUp() throws Exception {
        idExiste = 1L;
        idNaoExiste = 100L;
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void findAllTodosDeveriaRetornarListaDeClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        Assertions.assertFalse(clientes.isEmpty());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void findByIdPorIdDeveriaRetornarClienteQuandoIdExistir() {
        Cliente cliente = clienteRepository.findById(idExiste).orElse(null);

        Assertions.assertNotNull(cliente);
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void findByIdPorIdNaoDeveriaRetornarClienteQuandoIdNaoExistir() {
        Cliente cliente = clienteRepository.findById(idNaoExiste).orElse(null);

        Assertions.assertNull(cliente);
    }

    @Test
    void saveDeveriaInserirCliente() {
        Cliente cliente = ClienteFactory.clienteFactory();

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Assertions.assertNotNull(clienteSalvo);
        Assertions.assertNotNull(clienteSalvo.getId());
    }

    @Sql(statements = "INSERT INTO cliente (id, nome) VALUES (1, 'Cliente 1')")
    @Test
    void deleteByIdDeveriaDeletarClienteQuandoIdExistir() {
        clienteRepository.deleteById(idExiste);

        Cliente cliente = clienteRepository.findById(idExiste).orElse(null);

        Assertions.assertNull(cliente);
    }

}
