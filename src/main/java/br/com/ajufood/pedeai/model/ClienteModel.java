package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class ClienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O Nome é obrigatório.")
    @Column(name = "nome", nullable = false, length = 128)
    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido!")
    @Length(min = 11, max = 11, message = "O CPF deverá ter obrigatoriamente 11 dígitos.")
    @Column(name = "cpf", nullable = false, length = 11,  unique = true)
    private String cpf;

    @NotBlank(message = "O E-mail é obrigatório.")
    @Email(message = "E-mail inválido!")
    @Length(min = 3, max = 256, message = "O E-mail deverá ter no mínimo 3 caracteres e no máximo 256 caracteres.")
    @Column(name = "email", nullable = false, length = 256, unique = true)
    private String email;

    @NotBlank(message = "O Telefone é obrigatório.")
    @Column(name = "telefone", nullable = false, length = 11)
    @Length(min = 11, max = 11, message = "O telefone deverá ter obrigatoriamente 11 dígitos.")
    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnderecoModel> enderecos = new ArrayList<>();

    public void addEndereco(EnderecoModel endereco) {
        this.enderecos.add(endereco);
        endereco.setCliente(this);
    }

    public void removeEndereco(EnderecoModel endereco) {
        this.enderecos.remove(endereco);
        endereco.setCliente(null);
    }
}