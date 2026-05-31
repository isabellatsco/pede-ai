package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "produto")
public class ProdutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O Nome é obrigatório.")
    @Column(name = "nome", nullable = false, length = 128)
    @Length(min = 2, max = 128, message = "O Nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @Length(max=256)
    @Column(name="descricao", length=256)
    private String descricao;

    @NotNull(message="O preço é obrigatório")
    @Column(name="preco", nullable=false, precision=11, scale=2)
    private BigDecimal preco;

    @NotNull(message="A disponibilidade é obrigatória")
    @Column(name="disponivel", nullable=false)
    private Boolean disponivel;

    @NotNull(message="O produto deve ter uma categoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoriaProdutoID", nullable=false)
    private CategoriaProdutoModel categoria;
}