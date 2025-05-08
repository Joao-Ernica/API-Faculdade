package API.repository;

import API.entites.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProdutoRepository {

	private final JdbcTemplate jdbcTemplate;

	private final RowMapper<Produto> rowMapper = (rs, rowNum) ->
			Produto.builder()
					.id(rs.getInt("id"))
					.nome(rs.getString("nome"))
					.descricao(rs.getString("descricao"))
					.preco(rs.getBigDecimal("preco"))
					.estoque(rs.getInt("estoque"))
					.build();

	public List<Produto> findAll() {
		String sql = "SELECT * FROM produtos";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public Optional<Produto> findById(Integer id) {
		String sql = "SELECT * FROM produtos WHERE id = ?";
		List<Produto> results = jdbcTemplate.query(sql, rowMapper, id);
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Produto save(Produto produto) {
		if (produto.getId() == null) {
			return insert(produto);
		} else {
			return update(produto);
		}
	}

	private Produto insert(Produto produto) {
		String sql = "INSERT INTO produtos (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getDescricao());
			ps.setBigDecimal(3, produto.getPreco());
			ps.setInt(4, produto.getEstoque() != null ? produto.getEstoque() : 0);
			return ps;
		}, keyHolder);

		produto.setId(keyHolder.getKey().intValue());
		return produto;
	}

	private Produto update(Produto produto) {
		String sql = "UPDATE produtos SET nome = ?, descricao = ?, preco = ?, estoque = ? WHERE id = ?";

		int rowsAffected = jdbcTemplate.update(sql,
				produto.getNome(),
				produto.getDescricao(),
				produto.getPreco(),
				produto.getEstoque(),
				produto.getId());

		if (rowsAffected == 0) {
			return null;
		}

		return produto;
	}

	public boolean delete(Integer id) {
		String sql = "DELETE FROM produtos WHERE id = ?";
		int rowsAffected = jdbcTemplate.update(sql, id);
		return rowsAffected > 0;
	}
}