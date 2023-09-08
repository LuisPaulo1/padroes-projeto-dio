package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepService viaCepService;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Cliente cliente = buscarCliente(id);
		return cliente;
	}

	@Transactional
	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Transactional
	@Override
	public void atualizar(Long id, Cliente cliente) {
		Cliente clienteBd = buscarCliente(id);;
		salvarClienteComCep(clienteBd);
	}

	@Transactional
	@Override
	public void deletar(Long id) {
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			enderecoRepository.flush();
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}

	private Cliente buscarCliente(Long id){
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Cliente com o id %d", id)));
		return cliente;
	}


}
