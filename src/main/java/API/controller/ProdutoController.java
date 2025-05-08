package API.controller;

import API.entites.Produto;
import API.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

	private final ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<List<Produto>> findAll() {
		List<Produto> produtos = produtoService.findAll();
		return ResponseEntity.ok(produtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findById(@PathVariable Integer id) {
		Produto produto = produtoService.findById(id);
		return ResponseEntity.ok(produto);
	}

	@PostMapping
	public ResponseEntity<Produto> save(@RequestBody Produto produto) {
		if (produto.getEstoque() == null) {
			produto.setEstoque(0);
		}

		produtoService.validarProduto(produto);

		produto.setId(null);

		Produto savedProduto = produtoService.save(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> update(@PathVariable Integer id, @RequestBody Produto produto) {
		produtoService.findById(id);

		// Validar o produto
		produtoService.validarProduto(produto);

		produto.setId(id);
		Produto updatedProduto = produtoService.save(produto);
		return ResponseEntity.ok(updatedProduto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		produtoService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/estoque")
	public ResponseEntity<Produto> atualizarEstoque(@PathVariable Integer id, @RequestBody Map<String, Integer> payload) {
		Integer novoEstoque = payload.get("estoque");

		if (novoEstoque == null) {
			throw new IllegalArgumentException("A quantidade de estoque deve ser informada");
		}

		produtoService.atualizarEstoque(id, novoEstoque);
		Produto produto = produtoService.findById(id);
		return ResponseEntity.ok(produto);
	}
}