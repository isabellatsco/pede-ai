package br.com.ajufood.pedeai.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private List<EnderecoResponseDTO> enderecos = new ArrayList<>();
}