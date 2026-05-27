package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoRequestDTO {

    @NotNull(message = "O valor pago é obrigatório")
    @Positive
    private BigDecimal valorPago;

    @NotNull(message = "A data e hora do pagamento são obrigatórias")
    private LocalDateTime dataHora;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private Integer formaPagamentoId;

}