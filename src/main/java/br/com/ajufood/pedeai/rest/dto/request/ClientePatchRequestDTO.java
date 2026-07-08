package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientePatchRequestDTO {

    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @Email(message = "E-mail inválido!")
    @Length(min = 3, max = 256, message = "O E-mail deverá ter no mínimo 3 caracteres e no máximo 256 caracteres.")
    private String email;

    @Length(min = 11, max = 11, message = "O telefone deverá ter obrigatoriamente 11 dígitos.")
    private String telefone;
}
