package br.com.ajufood.pedeai.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponseDTO {
    private int id;
    private BigDecimal valorPago;
    private LocalDateTime dataHora;
    private FormaPagamentoResponseDTO formaPagamento;
}