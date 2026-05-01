package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "formaPagamento")
public class FormaPagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O Nome é obrigatório.")
    @Column(name = "nome", nullable = false, length = 128)
    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @Length(max = 256)
    @Column(name = "descricao", length = 256)
    private String descricao;
}