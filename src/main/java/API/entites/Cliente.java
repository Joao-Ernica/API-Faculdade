package API.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

	private Integer id;
	private String nome;
	private String email;
	private String telefone;

	// Isso faz com que a senha seja incluída na deserialização (leitura),
	// mas excluída da serialização (resposta)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String senha;
}