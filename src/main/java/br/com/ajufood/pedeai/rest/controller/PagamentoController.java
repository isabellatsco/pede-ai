package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentosPedidoDTO;
import br.com.ajufood.pedeai.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Operation(summary = "Cadastra o pagamento de um pedido")
    @ApiResponse(responseCode = "201", description = "Pagamento processado com sucesso e status do pedido atualizado")
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> salvar(@Valid @RequestBody PagamentoRequestDTO dto) {
        PagamentoResponseDTO pagamento = pagamentoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }

    @Operation(summary = "Busca os pagamentos de um pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagamentosPedidoDTO> obterPorPedidoId(@PathVariable int pedidoId) {
        PagamentosPedidoDTO resumo = pagamentoService.obterPorPedidoId(pedidoId);
        return ResponseEntity.ok(resumo);
    }

    @Operation(summary = "Busca o pagamento pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> obterPorId(@PathVariable int id) {
        PagamentoResponseDTO pagamento = pagamentoService.obterPorId(id);
        return ResponseEntity.ok(pagamento);
    }
}