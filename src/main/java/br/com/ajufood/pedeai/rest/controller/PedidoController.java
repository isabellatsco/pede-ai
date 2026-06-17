package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.StatusPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoDTO;
import br.com.ajufood.pedeai.service.PedidoService;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Page<PedidoResponseDTO>> obterTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        return ResponseEntity.ok(pedidoService.obterTodos(pagina, tamanho));
    }

    @Operation(summary = "Lista pedidos de um cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoResumoDTO>> obterPorCliente(@PathVariable Integer clienteId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        Page<PedidoResumoDTO> historico = pedidoService.obterPorCliente(clienteId, status, pagina, tamanho);
        return ResponseEntity.ok(historico);
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