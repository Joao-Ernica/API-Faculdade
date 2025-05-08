package API.entites;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

	private Integer id;
	private String nome;
	private String descricao;
	private BigDecimal preco;
	private Integer estoque;

}