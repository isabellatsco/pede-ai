package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @NotNull(message = "A forma de pagamento é obrigatório")
    private int formaPagamentoId;

    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    @NotNull private LocalDateTime dataHora;

    @NotNull(message = "O status é obrigatório")
    @Length(max = 128)
    private String status;

    @Valid
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemPedidoRequestDTO> itens;
}