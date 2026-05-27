package br.com.ajufood.pedeai.rest.dto.request;

import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.model.ProdutoModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoRequestDTO {

    @NotNull(message = "A quantidade é obrigatória")
    private int quantidade;

    @NotNull(message = "O preço unitário é obrigatório")
    private BigDecimal precoUnitario;

    @NotNull(message = "O pedido é obrigatório")
    private PedidoModel pedido;

    @NotNull(message = "O produto é obrigatório")
    private ProdutoModel produto;

}
