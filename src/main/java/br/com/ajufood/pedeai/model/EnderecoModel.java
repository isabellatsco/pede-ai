package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "endereco")
public class EnderecoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message="O endereço é obrigatório")
    @Length(max=256, message="O endereço deve ter no máximo 256 caracteres")
    @Column(name="endereco", nullable=false, length=256)
    private String endereco;

    @NotNull(message="O número é obrigatório")
    @Column(name="numero", nullable=false)
    private Integer numero;

    @Length(max=256, message="Complemento deve ter no máximo 256 caracteres")
    @Column(name= "complemento", length=256)
    private String complemento;

    @NotBlank(message="O bairro é obrigatório")
    @Length(max=128, message="O bairro deve ter no máximo 128 caracteres")
    @Column(name="bairro", nullable=false, length=128)
    private String bairro;

    @NotBlank(message="A cidade é obrigatória")
    @Length(max=128, message="A cidade deve ter no máximo 128 caracteres")
    @Column(name="cidade", nullable=false, length=128)
    private String cidade;

    @NotBlank(message="O estado é obrigatório")
    @Length(min=2, max=2, message="O estado deve ter 2 caracteres")
    @Column(name="estado", nullable=false, length=2)
    private String estado;

    @NotBlank(message="O CEP é obrigatório")
    @Length(min=8, max=8, message="O CEP deve ter 8 caracteres")
    @Column(name="cep", nullable=false, length=8)
    private String cep;

    @NotNull(message="O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name="clienteID", nullable=false)
    private ClienteModel cliente;
}