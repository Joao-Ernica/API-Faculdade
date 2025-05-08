package API.controller;

import API.entites.Cliente;
import API.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;

	@GetMapping
	public ResponseEntity<List<Cliente>> findAll() {
		List<Cliente> clientes = clienteService.findAll();
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable Integer id) {
		Cliente cliente = clienteService.findById(id);
		return ResponseEntity.ok(cliente);
	}

	@PostMapping
	public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
		clienteService.validarCliente(cliente);

		cliente.setId(null);
		Cliente savedCliente = clienteService.save(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> update(@PathVariable Integer id, @RequestBody Cliente cliente) {
		clienteService.findById(id);

		clienteService.validarCliente(cliente);

		cliente.setId(id);
		Cliente updatedCliente = clienteService.save(cliente);
		return ResponseEntity.ok(updatedCliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
}