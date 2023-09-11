package one.digitalinnovation.gof.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClienteTestes {

    @Test
    public void clienteDeveriaTerUmaEstruturaCorreta() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setEndereco(new Endereco());

        Assertions.assertNotNull(cliente.getId());
        Assertions.assertNotNull(cliente.getNome());
        Assertions.assertNotNull(cliente.getEndereco());
    }
}
