package one.digitalinnovation.gof.util;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

public class ClienteFactory {

    private ClienteFactory() {
    }

    public static Cliente clienteFactory() {
        Endereco endereco = new Endereco();
        endereco.setCep("01000-100");
        Cliente cliente = new Cliente();
        cliente.setNome("João da Silva");
        cliente.setEndereco(endereco);
        return cliente;
    }

}
