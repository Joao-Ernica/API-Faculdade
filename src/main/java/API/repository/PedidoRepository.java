package API.repository;

import API.entites.ItemPedido;
import API.entites.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PedidoRepository {

	private final JdbcTemplate jdbcTemplate;

	private final RowMapper<Pedido> pedidoRowMapper = (rs, rowNum) ->
			Pedido.builder()
					.id(rs.getInt("id"))
					.clienteId(rs.getInt("cliente_id"))
					.dataPedido(rs.getTimestamp("data_pedido").toLocalDateTime())
					.status(rs.getString("status"))
					.build();

	private final RowMapper<ItemPedido> itemPedidoRowMapper = (rs, rowNum) ->
			ItemPedido.builder()
					.pedidoId(rs.getInt("pedido_id"))
					.produtoId(rs.getInt("produto_id"))
					.quantidade(rs.getInt("quantidade"))
					.precoUnitario(rs.getBigDecimal("preco_unitario"))
					.build();

	public List<Pedido> findAll() {
		String sql = "SELECT * FROM pedidos ORDER BY data_pedido DESC";
		List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper);

		// Carregar itens para cada pedido
		for (Pedido pedido : pedidos) {
			loadPedidoRelationships(pedido);
		}

		return pedidos;
	}

	public Optional<Pedido> findById(Integer id) {
		String sql = "SELECT * FROM pedidos WHERE id = ?";
		List<Pedido> results = jdbcTemplate.query(sql, pedidoRowMapper, id);

		if (results.isEmpty()) {
			return Optional.empty();
		}

		Pedido pedido = results.get(0);
		loadPedidoRelationships(pedido);

		return Optional.of(pedido);
	}

	private void loadPedidoRelationships(Pedido pedido) {
		String sql = "SELECT * FROM itens_pedido WHERE pedido_id = ?";
		List<ItemPedido> itens = jdbcTemplate.query(sql, itemPedidoRowMapper, pedido.getId());
		pedido.setItens(itens);
	}

	public Pedido save(Pedido pedido) {
		if (pedido.getId() == null) {
			return insert(pedido);
		} else {
			return update(pedido);
		}
	}

	private Pedido insert(Pedido pedido) {
		String sql = "INSERT INTO pedidos (cliente_id, data_pedido, status) VALUES (?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
			ps.setInt(1, pedido.getClienteId());
			ps.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido()));
			ps.setString(3, pedido.getStatus());
			return ps;
		}, keyHolder);

		Number key = (Number) keyHolder.getKeys().get("ID");
		pedido.setId(key.intValue());

		saveItensPedido(pedido);

		return pedido;
	}

	private Pedido update(Pedido pedido) {
		String sql = "UPDATE pedidos SET cliente_id = ?, data_pedido = ?, status = ? WHERE id = ?";

		int rowsAffected = jdbcTemplate.update(sql,
				pedido.getClienteId(),
				Timestamp.valueOf(pedido.getDataPedido()),
				pedido.getStatus(),
				pedido.getId());

		if (rowsAffected == 0) {
			return null;
		}

		jdbcTemplate.update("DELETE FROM itens_pedido WHERE pedido_id = ?", pedido.getId());

		saveItensPedido(pedido);

		return pedido;
	}

	private void saveItensPedido(Pedido pedido) {
		String sql = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

		for (ItemPedido item : pedido.getItens()) {
			item.setPedidoId(pedido.getId());
			jdbcTemplate.update(sql,
					item.getPedidoId(),
					item.getProdutoId(),
					item.getQuantidade(),
					item.getPrecoUnitario());
		}
	}

	public void delete(Integer id) {
		String sql = "DELETE FROM pedidos WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);
		if (linhasAfetadas == 0) {
			throw new RuntimeException("Falha ao excluir pedido: nenhuma linha afetada");
		}
	}

	public Pedido atualizarStatus(Integer id, String novoStatus) {
		String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
		jdbcTemplate.update(sql, novoStatus, id);

		return findById(id).orElse(null);
	}
}