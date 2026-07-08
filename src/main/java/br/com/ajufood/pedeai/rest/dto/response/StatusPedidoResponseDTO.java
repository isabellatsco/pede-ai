package br.com.ajufood.pedeai.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusPedidoResponseDTO {
    private Integer id;
    private String statusAnterior;
    private String statusAtual;
    private LocalDateTime dataHora;
}
