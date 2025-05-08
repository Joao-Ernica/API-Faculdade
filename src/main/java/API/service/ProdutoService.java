package API.service;

import API.Excecoes.DatabaseException;
import API.Excecoes.ResourceNotFoundException;
import API.entites.Produto;
import API.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	public List<Produto> findAll() {
		return produtoRepository.findAll();
	}

	public Produto findById(Integer id) {
		return produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
	}

	@Transactional
	public Produto save(Produto produto) {
		try {
			return produtoRepository.save(produto);
		} catch (Exception e) {
			throw new DatabaseException("Erro ao salvar produto: " + e.getMessage());
		}
	}

	@Transactional
	public void delete(Integer id) {
		if (!produtoRepository.findById(id).isPresent()) {
			throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
		}

		try {
			produtoRepository.delete(id);
		} catch (Exception e) {
			throw new DatabaseException("Não foi possível excluir o produto: " + e.getMessage());
		}
	}

	@Transactional
	public void atualizarEstoque(Integer produtoId, Integer quantidade) {
		if (quantidade < 0) {
			throw new IllegalArgumentException("A quantidade não pode ser negativa");
		}

		Produto produto = findById(produtoId);
		produto.setEstoque(quantidade);
		save(produto);
	}

	@Transactional
	public void incrementarEstoque(Integer produtoId, Integer quantidade) {
		if (quantidade < 0) {
			throw new IllegalArgumentException("O incremento não pode ser negativo");
		}

		Produto produto = findById(produtoId);
		produto.setEstoque(produto.getEstoque() + quantidade);
		save(produto);
	}

	public void validarProduto(Produto produto) {
		if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("O nome do produto é obrigatório");
		}

		if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
		}

		if (produto.getEstoque() != null && produto.getEstoque() < 0) {
			throw new IllegalArgumentException("O estoque não pode ser negativo");
		}
	}
}