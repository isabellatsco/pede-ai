package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.CategoriaProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.CategoriaProdutoResponseDTO;
import br.com.ajufood.pedeai.service.CategoriaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoService categoriaProdutoService;

    @Operation(summary = "Busca uma categoria pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> obterPorId(@PathVariable Integer id) {
        CategoriaProdutoResponseDTO categoriaResponseDTO = categoriaProdutoService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaResponseDTO);
    }

    @Operation(summary = "Lista todas as categorias")
    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> obterTodos() {
        List<CategoriaProdutoResponseDTO> categoriasResponseDTO = categoriaProdutoService.obterTodos();
        return ResponseEntity.ok(categoriasResponseDTO);
    }

    @Operation(summary = "Cadastra uma nova categoria")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @PostMapping
    public ResponseEntity<CategoriaProdutoResponseDTO> salvar(@Valid @RequestBody CategoriaProdutoRequestDTO categoriaRequestDTO) {
        CategoriaProdutoResponseDTO categoriaNova = categoriaProdutoService.salvar(categoriaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNova);
    }

    @Operation(summary = "Atualiza uma categoria existente")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaProdutoRequestDTO categoriaRequestDTO) {
        CategoriaProdutoResponseDTO categoriaAtualizadaDTO = categoriaProdutoService.atualizar(id, categoriaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizadaDTO);
    }

    @Operation(summary = "Remove uma categoria pelo id")
    @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        categoriaProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}