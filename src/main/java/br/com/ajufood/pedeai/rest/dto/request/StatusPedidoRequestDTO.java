package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusPedidoRequestDTO {
    @NotBlank(message = "O status é obrigatório")
    private String status;
}
