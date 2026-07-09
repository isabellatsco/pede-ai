package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoDisponibilidadeRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import br.com.ajufood.pedeai.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Busca um produto pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable int id) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(produtoResponseDTO);
    }

    @Operation(summary = "Lista todos os produtos")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> obterTodos(
            @RequestParam(name = "categoriaId", required = false) Integer categoriaId) {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoService.obterTodos(categoriaId);
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

    @Operation(summary = "Altera a disponibilidade de um produto")
    @ApiResponse(responseCode = "200", description = "Disponibilidade do produto alterada com sucesso")
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarDisponibilidade(
            @PathVariable int id,
            @RequestBody @Valid ProdutoDisponibilidadeRequestDTO request) {
        ProdutoResponseDTO produtoAtualizadoDTO = produtoService.atualizarDisponibilidade(id, request.getDisponivel());
        return ResponseEntity.status(HttpStatus.OK).body(produtoAtualizadoDTO);
    }
}
