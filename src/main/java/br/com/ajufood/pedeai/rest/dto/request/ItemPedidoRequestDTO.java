package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoRequestDTO {

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value=1, message = "A quantidade deve ser no mínimo 1")
    private int quantidade;

    @NotNull(message = "O produto é obrigatório")
    private int produtoId;
}