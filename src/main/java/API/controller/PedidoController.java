package API.controller;

import API.entites.Pedido;
import API.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

	private final PedidoService pedidoService;

	@GetMapping
	public ResponseEntity<List<Pedido>> findAll() {
		return ResponseEntity.ok(pedidoService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable Integer id) {
		Pedido pedido = pedidoService.findById(id);
		return ResponseEntity.ok(pedido);
	}

	@PostMapping
	public ResponseEntity<Pedido> save(@RequestBody Pedido pedido) {
		pedidoService.validarPedido(pedido);

		pedido.setId(null);
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setStatus("PENDENTE");

		Pedido savedPedido = pedidoService.save(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPedido);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody Pedido pedido) {
		pedidoService.findById(id);

		pedidoService.validarPedido(pedido);

		pedido.setId(id);
		Pedido updatedPedido = pedidoService.save(pedido);
		return ResponseEntity.ok(updatedPedido);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Pedido> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
		String novoStatus = payload.get("status");

		if (novoStatus == null) {
			throw new IllegalArgumentException("O status é obrigatório");
		}

		Pedido pedido = pedidoService.atualizarStatus(id, novoStatus);
		return ResponseEntity.ok(pedido);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		pedidoService.delete(id);
		return ResponseEntity.noContent().build();
	}
}