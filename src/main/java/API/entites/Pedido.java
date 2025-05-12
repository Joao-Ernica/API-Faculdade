package API.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

	private Integer id;
	private Integer clienteId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT")
	private LocalDateTime dataPedido;

	private String status;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Cliente cliente;

	@Builder.Default
	private List<ItemPedido> itens = new ArrayList<>();

	public BigDecimal getTotal() {
		return itens.stream()
				.map(ItemPedido::getSubtotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}