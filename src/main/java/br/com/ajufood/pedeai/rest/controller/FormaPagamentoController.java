package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import br.com.ajufood.pedeai.service.FormaPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formas-pagamento")
@RequiredArgsConstructor
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;

    @Operation(summary = "Busca uma forma de pagamento pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> obterPorId(@PathVariable int id) {
        return ResponseEntity.ok(formaPagamentoService.obterPorId(id));
    }

    @Operation(summary = "Lista todas as formas de pagamento")
    @GetMapping
    public ResponseEntity<List<FormaPagamentoResponseDTO>> obterTodas() {
        return ResponseEntity.ok(formaPagamentoService.obterTodas());
    }

    @Operation(summary = "Cadastra uma nova forma de pagamento")
    @ApiResponse(responseCode = "201", description = "Forma de pagamento criada com sucesso")
    @PostMapping
    public ResponseEntity<FormaPagamentoResponseDTO> salvar(@Valid @RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO novaForma = formaPagamentoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaForma);
    }

    @Operation(summary = "Atualiza uma forma de pagamento existente")
    @ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO atualizada = formaPagamentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(summary = "Remove uma forma de pagamento pelo id")
    @ApiResponse(responseCode = "204", description = "Forma de pagamento removida com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        formaPagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}