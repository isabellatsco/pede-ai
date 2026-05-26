package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Busca um cliente pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterPorId(@PathVariable int id) {
        ClienteResponseDTO clienteResponseDTO = clienteService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDTO);
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obterTodos() {
        List<ClienteResponseDTO> clienteResponseDTOS = clienteService.obterTodos();
        return ResponseEntity.ok(clienteResponseDTOS);
    }

    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteNovo = clienteService.salvar(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteNovo);
    }

    @Operation(summary = "Atualiza um cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable int id,
            @RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteAtualizadoDTO = clienteService.atualizar(id, clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizadoDTO);
    }

    @Operation(summary = "Remove um cliente pelo id")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
