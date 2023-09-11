package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import one.digitalinnovation.gof.service.impl.ClienteServiceImpl;
import one.digitalinnovation.gof.util.ClienteFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ClienteServiceTesteUnitario {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ViaCepService viaCepService;

    private Long idExiste;
    private Long idNaoExiste;

    @BeforeEach
    void setUp() throws Exception {
        idExiste = 1L;
        idNaoExiste = 100L;
        Cliente cliente = ClienteFactory.clienteFactory();
        List<Cliente> clientes = List.of(cliente);

        when(clienteRepository.findAll()).thenReturn(clientes);

        when(clienteRepository.findById(idExiste)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findById(idNaoExiste)).thenReturn(Optional.empty());

        when(clienteRepository.save(any())).thenReturn(cliente);

        doNothing().when(clienteRepository).deleteById(idExiste);
    }

    @Test
    void buscarTodosDeveriaRetornarListaDeClientes() {
        List<Cliente> clientes = clienteService.buscarTodos();

        Assertions.assertFalse(clientes.isEmpty());
    }

    @Test
    void buscarPorIdDeveriaRetornarCliente() {
        Cliente cliente = clienteService.buscarPorId(idExiste);

        Assertions.assertNotNull(cliente);
    }

    @Test
    public void buscarPorIdDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
            clienteService.buscarPorId(idNaoExiste);
        });
    }

    @Test
    void inserirDeveriaInserirCliente() {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");

        cliente = clienteService.inserir(cliente);

        Assertions.assertNotNull(cliente);
    }

    @Test
    void atualizarDeveriaAtualizarCliente() {
        Cliente cliente = ClienteFactory.clienteFactory();
        cliente.getEndereco().setCep("01001000");

        cliente = clienteService.atualizar(idExiste, cliente);

        Assertions.assertEquals(cliente.getNome(), "João da Silva");
    }

    @Test
    public void deletarNaoDeveriaLancarExceptionQuandoIdExistir() {
        Assertions.assertDoesNotThrow(() -> {
            clienteService.deletar(idExiste);
        });
    }
}
