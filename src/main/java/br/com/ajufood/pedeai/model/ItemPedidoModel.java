package br.com.ajufood.pedeai.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itensPedido")
public class ItemPedidoModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull(message="A quantidade é obrigatória")
    @Positive(message="A quantidade deve ser maior que zero")
    @Column(name="quantidade", nullable=false)
    private Integer quantidade;

    @NotNull(message="O preço unitário é obrigatório")
    @Column(name="precoUnitario", nullable=false, precision=11, scale=2)
    private BigDecimal precoUnitario;

    @NotNull(message="O subtotal é obrigatório")
    @Column(name="subTotal", nullable=false, precision=11, scale=2)
    private BigDecimal subTotal;

    @NotNull(message="O pedido é obrigatório")
    @ManyToOne
    @JoinColumn(name="pedidoID", nullable=false)
    private PedidoModel pedido;

    @NotNull(message="O produto é obrigatório")
    @ManyToOne
    @JoinColumn(name="produtoID", nullable=false)
    private ProdutoModel produto;

    public void calcularSubTotal() {
        this.subTotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }

    public BigDecimal getSubTotal() {
        calcularSubTotal();
        return this.subTotal;
    }

}
