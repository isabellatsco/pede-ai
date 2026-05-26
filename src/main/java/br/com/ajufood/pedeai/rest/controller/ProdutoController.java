package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import br.com.ajufood.pedeai.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Busca um produto pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable int id) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDTO);
    }

    @Operation(summary = "Lista todos os produtos")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> obterTodos() {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoService.obterTodos();
        return ResponseEntity.ok(produtoResponseDTOS);
    }

    @Operation(summary = "Cadastra um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO produtoNovo = produtoService.salvar(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoNovo);
    }

    @Operation(summary = "Atualiza um produto existente")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable int id,
            @RequestBody @Valid ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO produtoAtualizadoDTO = produtoService.atualizar(id, produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(produtoAtualizadoDTO);
    }

    @Operation(summary = "Remove um produto pelo id")
    @ApiResponse(responseCode = "204", description = "Produto removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
