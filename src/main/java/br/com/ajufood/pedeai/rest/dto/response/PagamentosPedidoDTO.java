package br.com.ajufood.pedeai.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentosPedidoDTO {
    private List<PagamentoResponseDTO> pagamentos;
}
