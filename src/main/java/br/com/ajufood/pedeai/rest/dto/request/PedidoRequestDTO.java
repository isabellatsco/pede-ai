package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {
    @NotNull(message = "O cliente é obrigatório")
    private int clienteId;

    @NotNull(message = "O endereço de entrega é obrigatório")
    private int enderecoEntregaId;
    
    @Valid
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemPedidoRequestDTO> itens;
}