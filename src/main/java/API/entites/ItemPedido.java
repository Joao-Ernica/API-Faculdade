package API.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

	private Integer pedidoId;
	private Integer produtoId;
	private Integer quantidade;
	private BigDecimal precoUnitario;
	private Produto produto;

	public BigDecimal getSubtotal() {
		return precoUnitario.multiply(new BigDecimal(quantidade));
	}
}