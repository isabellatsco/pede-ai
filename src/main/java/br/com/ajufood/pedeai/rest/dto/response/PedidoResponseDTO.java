package br.com.ajufood.pedeai.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private int id;
    private LocalDateTime dataHora;
    private String status;
    private BigDecimal valorTotal;
    private ClienteResponseDTO cliente;
    private EnderecoResponseDTO enderecoEntrega;
    private List<ItemPedidoResponseDTO> itens;
    private List<PagamentoResponseDTO> pagamentos;
}