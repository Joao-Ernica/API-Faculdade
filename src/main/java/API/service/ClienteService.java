package API.service;

import API.Excecoes.DatabaseException;
import API.Excecoes.ResourceNotFoundException;
import API.entites.Cliente;
import API.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public List<Cliente> findAll() {
		try {
			return clienteRepository.findAll();
		} catch (Exception e) {
			throw new DatabaseException("Erro ao buscar clientes: " + e.getMessage());
		}
	}

	public Cliente findById(Integer id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		try {
			Cliente savedCliente = clienteRepository.save(cliente);
			if (savedCliente == null) {
				throw new ResourceNotFoundException("Cliente não encontrado para atualização: " + cliente.getId());
			}
			return savedCliente;
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Erro ao salvar cliente: " + e.getMessage());
		}
	}

	@Transactional
	public void delete(Integer id) {
		findById(id);
		try {
			clienteRepository.delete(id);
		} catch (Exception e) {
			throw new DatabaseException("Erro ao excluir cliente: " + e.getMessage());
		}
	}

	public void validarCliente(Cliente cliente) {
		if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("O nome do cliente é obrigatório");
		}

		if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("O email do cliente é obrigatório");
		}

		if (!isValidEmail(cliente.getEmail())) {
			throw new IllegalArgumentException("O email informado não é válido");
		}
	}

	private boolean isValidEmail(String email) {
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		return email.matches(regex);
	}
}