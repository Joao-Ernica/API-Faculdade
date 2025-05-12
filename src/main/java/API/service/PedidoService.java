package API.service;

import API.Excecoes.DatabaseException;
import API.Excecoes.ResourceNotFoundException;
import API.entites.ItemPedido;
import API.entites.Pedido;
import API.entites.Produto;
import API.repository.PedidoRepository;
import API.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteService clienteService;
	private final ProdutoService produtoService;

	public List<Pedido> findAll() {
		try {
			return pedidoRepository.findAll();
		} catch (Exception e) {
			throw new DatabaseException("Erro ao buscar pedidos: " + e.getMessage());
		}
	}

	public Pedido findById(Integer id) {
		return pedidoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
	}

	@Transactional
	public Pedido save(Pedido pedido) {
		try {
			if (pedido.getId() != null) {
				Pedido pedidoExistente = findById(pedido.getId());

				if (pedido.getDataPedido() == null) {
					pedido.setDataPedido(pedidoExistente.getDataPedido());
				}
			}

			verificarEstoqueDisponivel(pedido);
			Pedido savedPedido = pedidoRepository.save(pedido);
			if (savedPedido == null) {
				throw new ResourceNotFoundException("Pedido não encontrado para atualização: " + pedido.getId());
			}
			reduzirEstoqueAposVenda(savedPedido);
			return savedPedido;
		} catch (ResourceNotFoundException | IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Erro ao salvar pedido: " + e.getMessage());
		}
	}

	private void verificarEstoqueDisponivel(Pedido pedido) {
		for (ItemPedido item : pedido.getItens()) {
			Produto produto = produtoService.findById(item.getProdutoId());

			if (produto.getEstoque() < item.getQuantidade()) {
				throw new IllegalArgumentException(
						"Estoque insuficiente para o produto: " + produto.getNome() +
								". Disponível: " + produto.getEstoque() +
								", Solicitado: " + item.getQuantidade()
				);
			}
		}
	}

	@Transactional
	public void delete(Integer id) {
		findById(id);
		try {
			pedidoRepository.delete(id);
		} catch (Exception e) {
			throw new DatabaseException("Erro ao excluir pedido: " + e.getMessage());
		}
	}

	@Transactional
	public Pedido atualizarStatus(Integer id, String novoStatus) {
		Pedido pedido = findById(id);

		if (!isValidStatus(novoStatus)) {
			throw new IllegalArgumentException("Status inválido: " + novoStatus);
		}

		try {
			return pedidoRepository.atualizarStatus(id, novoStatus);
		} catch (Exception e) {
			throw new DatabaseException("Erro ao atualizar status do pedido: " + e.getMessage());
		}
	}

	private boolean isValidStatus(String status) {
		return status != null && List.of("PENDENTE", "PAGO", "ENVIADO", "ENTREGUE", "CANCELADO")
				.contains(status.toUpperCase());
	}

	public void validarPedido(Pedido pedido) {
		if (pedido.getClienteId() == null) {
			throw new IllegalArgumentException("O cliente é obrigatório");
		}

		clienteService.findById(pedido.getClienteId());

		if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
			throw new IllegalArgumentException("O pedido deve conter pelo menos um item");
		}

		for (ItemPedido item : pedido.getItens()) {
			if (item.getProdutoId() == null) {
				throw new IllegalArgumentException("Todos os itens devem especificar um produto");
			}

			if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
				throw new IllegalArgumentException("A quantidade de cada item deve ser maior que zero");
			}

			Produto produto = produtoService.findById(item.getProdutoId());
			item.setPrecoUnitario(produto.getPreco());
		}
	}

	private void reduzirEstoqueAposVenda(Pedido pedido) {
		for (ItemPedido item : pedido.getItens()) {
			Produto produto = produtoService.findById(item.getProdutoId());
			int novoEstoque = produto.getEstoque() - item.getQuantidade();
			produtoService.atualizarEstoque(item.getProdutoId(), novoEstoque);
		}
	}
}