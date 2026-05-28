package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.StatusPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired private PedidoService pedidoService;

    @Operation(summary = "Busca um pedido pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obterPorId(@PathVariable int id) {
        return ResponseEntity.ok(pedidoService.obterPorId(id));
    }

    @Operation(summary = "Lista todos os pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obterTodos() {
        return ResponseEntity.ok(pedidoService.obterTodos());
    }

    @Operation(summary = "Lista pedidos de um cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponseDTO>> obterPorCliente(@PathVariable int clienteId) {
        return ResponseEntity.ok(pedidoService.obterPorCliente(clienteId));
    }

    @Operation(summary = "Cria um novo pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.salvar(dto));
    }

    @Operation(summary = "Atualiza o status do pedido")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable int id,
            @Valid @RequestBody StatusPedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, dto.getStatus()));
    }

    @Operation(summary = "Remove um pedido pelo id")
    @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}