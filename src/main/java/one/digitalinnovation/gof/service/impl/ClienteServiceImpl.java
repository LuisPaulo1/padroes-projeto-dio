package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.service.exception.EntidadeNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepService viaCepService;

	@Override
	public List<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
        return buscarCliente(id);
	}

	@Transactional
	@Override
	public Cliente inserir(Cliente cliente) {
		 return salvarClienteComCep(cliente);
	}

	@Transactional
	@Override
	public Cliente atualizar(Long id, Cliente cliente) {
		Cliente clienteAtual = buscarCliente(id);
		BeanUtils.copyProperties(cliente, clienteAtual, "id");
		return salvarClienteComCep(clienteAtual);
	}

	@Transactional
	@Override
	public void deletar(Long id) {
		try {
			clienteRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Cliente com o id: %d não encontrado", id));
		}
	}

	private Cliente salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		if(cep == null || cep.isEmpty()) {
			throw new IllegalArgumentException("CEP não pode ser nulo ou vazio");
		}
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			if(novoEndereco != null && novoEndereco.getCep() == null) {
				throw new IllegalArgumentException(String.format("CEP: %s inválido", cep));
			}
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		return clienteRepository.save(cliente);
	}

	private Cliente buscarCliente(Long id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Cliente com o id: %d não encontrado", id)));
		return cliente;
	}

}
