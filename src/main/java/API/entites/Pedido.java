package API.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private LocalDateTime dataPedido;
	private String status;
	private Cliente cliente;

	@Builder.Default
	private List<ItemPedido> itens = new ArrayList<>();
}