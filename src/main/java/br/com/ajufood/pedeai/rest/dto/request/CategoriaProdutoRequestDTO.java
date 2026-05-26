package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaProdutoRequestDTO {
    @NotBlank(message = "O Nome é obrigatório.")
    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @Length(max = 256)
    private String descricao;
}
