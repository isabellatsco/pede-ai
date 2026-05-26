package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoRequestDTO {
    @NotBlank(message="O endereço é obrigatório")
    @Length(max=256, message="O endereço deve ter no máximo 256 caracteres")
    private String endereco;

    @NotNull(message="O número é obrigatório")
    private Integer numero;

    @Length(max=256, message="Complemento deve ter no máximo 256 caracteres")
    private String complemento;

    @NotBlank(message="O bairro é obrigatório")
    @Length(max=128, message="O bairro deve ter no máximo 128 caracteres")
    private String bairro;

    @NotBlank(message="A cidade é obrigatória")
    @Length(max=128, message="A cidade deve ter no máximo 128 caracteres")
    private String cidade;

    @NotBlank(message="O estado é obrigatório")
    @Length(min=2, max=2, message="O estado deve ter 2 caracteres")
    private String estado;

    @NotBlank(message="O CEP é obrigatório")
    @Length(min=8, max=8, message="O CEP deve ter 8 caracteres")
    private String cep;
}
