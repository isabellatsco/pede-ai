package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequestDTO {
    @NotBlank(message = "O Nome é obrigatório.")
    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @Length(max = 256)
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    private BigDecimal preco;

    @NotNull(message = "A disponibilidade é obrigatória.")
    private Boolean disponivel;

    @NotNull(message = "O produto deve ter uma categoria.")
    private Integer categoriaId;
}
