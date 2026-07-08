package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDisponibilidadeRequestDTO {
    @NotNull(message = "A disponibilidade é obrigatória.")
    private Boolean disponivel;
}
