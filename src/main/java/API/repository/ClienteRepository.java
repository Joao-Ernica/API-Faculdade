package API.repository;

import API.entites.Cliente;
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
public class ClienteRepository {

	private final JdbcTemplate jdbcTemplate;

	private final RowMapper<Cliente> rowMapper = (rs, rowNum) ->
			Cliente.builder()
					.id(rs.getInt("id"))
					.nome(rs.getString("nome"))
					.email(rs.getString("email"))
					.telefone(rs.getString("telefone"))
					.senha(rs.getString("senha"))
					.build();

	public List<Cliente> findAll() {
		return jdbcTemplate.query("SELECT * FROM clientes", rowMapper);
	}

	public Optional<Cliente> findById(Integer id) {
		List<Cliente> results = jdbcTemplate.query(
				"SELECT * FROM clientes WHERE id = ?",
				rowMapper,
				id
		);

		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Cliente save(Cliente cliente) {
		if (cliente.getId() == null) {
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO clientes (nome, email, telefone, senha) VALUES (?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS
				);
				ps.setString(1, cliente.getNome());
				ps.setString(2, cliente.getEmail());
				ps.setString(3, cliente.getTelefone());
				ps.setString(4, cliente.getSenha());
				return ps;
			}, keyHolder);

			Number key = (Number) keyHolder.getKeys().get("ID");
			cliente.setId(key.intValue());
		} else {
			int linhasAfetadas;

			if (cliente.getSenha() != null && !cliente.getSenha().isEmpty()) {
				linhasAfetadas = jdbcTemplate.update(
						"UPDATE clientes SET nome = ?, email = ?, telefone = ?, senha = ? WHERE id = ?",
						cliente.getNome(),
						cliente.getEmail(),
						cliente.getTelefone(),
						cliente.getSenha(),
						cliente.getId()
				);
			} else {
				linhasAfetadas = jdbcTemplate.update(
						"UPDATE clientes SET nome = ?, email = ?, telefone = ? WHERE id = ?",
						cliente.getNome(),
						cliente.getEmail(),
						cliente.getTelefone(),
						cliente.getId()
				);
			}

			if (linhasAfetadas == 0) {
				return null;
			}
		}

		return cliente;
	}

	public void delete(Integer id) {
		int linhasAfetadas = jdbcTemplate.update("DELETE FROM clientes WHERE id = ?", id);
		if (linhasAfetadas == 0) {
			throw new RuntimeException("Falha ao excluir cliente: nenhuma linha afetada");
		}
	}

	public Optional<Cliente> findByEmail(String email) {
		List<Cliente> results = jdbcTemplate.query(
				"SELECT * FROM clientes WHERE email = ?",
				rowMapper,
				email
		);

		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}
}