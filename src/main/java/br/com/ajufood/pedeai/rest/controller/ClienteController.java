package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ClientePatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
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
        return ResponseEntity.ok(clienteResponseDTO);
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
    @ApiResponse(responseCode = "200", description = "Cliente updated com sucesso")
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable int id,
            @RequestBody @Valid ClientePatchRequestDTO clientePatchRequestDTO) {
        ClienteResponseDTO clienteAtualizadoDTO = clienteService.atualizar(id, clientePatchRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizadoDTO);
    }

    @Operation(summary = "Remove um cliente pelo id")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista todos os endereços do cliente")
    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<EnderecoResponseDTO>> obterEnderecos(@PathVariable int id) {
        List<EnderecoResponseDTO> enderecos = clienteService.obterEnderecos(id);
        return ResponseEntity.ok(enderecos);
    }

    @Operation(summary = "Busca um endereço do cliente pelo id")
    @GetMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<EnderecoResponseDTO> obterEnderecoPorId(@PathVariable int id, @PathVariable int enderecoId) {
        EnderecoResponseDTO endereco = clienteService.obterEnderecoPorId(id, enderecoId);
        return ResponseEntity.ok(endereco);
    }

    @Operation(summary = "Cadastra um novo endereço ao cliente")
    @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso")
    @PostMapping("/{id}/enderecos")
    public ResponseEntity<EnderecoResponseDTO> salvarEndereco(
            @PathVariable int id,
            @Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO novoEndereco = clienteService.salvarEndereco(id, enderecoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @Operation(summary = "Atualiza um endereço do cliente")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @PutMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable int id,
            @PathVariable int enderecoId,
            @Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO enderecoAtualizado = clienteService.atualizarEndereco(id, enderecoId, enderecoRequestDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @Operation(summary = "Remove um endereço do cliente")
    @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso")
    @DeleteMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable int id, @PathVariable int enderecoId) {
        clienteService.deletarEndereco(id, enderecoId);
        return ResponseEntity.noContent().build();
    }
}