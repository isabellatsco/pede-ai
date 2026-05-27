package br.com.ajufood.pedeai.rest.dto.response;

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
public class PagamentoResponseDTO {
    private int id;
    private BigDecimal valorPago;
    private LocalDateTime dataHora;
    private FormaPagamentoResponseDTO formaPagamento;
}