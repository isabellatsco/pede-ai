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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pedido")
public class PedidoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message="A data e hora são obrigatórias")
    @Column(name="data_hora", nullable=false)
    private LocalDateTime dataHora;

    @NotBlank(message="O status é obrigatório")
    @Length(max=128)
    @Column(name="status", nullable=false, length=128)
    private String status;

    @NotNull(message="O valor total é obrigatório")
    @Column(name="valor_total", nullable=false, precision=11, scale=2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @NotNull(message="O cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cliente_id", nullable=false)
    private ClienteModel cliente;

    @NotNull(message="O endereço de entrega é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="endereco_id", nullable=false)
    private EnderecoModel enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoModel> itens = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagamentoModel> pagamentos = new ArrayList<>();

    public void atualizarValorTotal() {
        this.valorTotal = this.itens.stream()
            .map(ItemPedidoModel::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void adicionarItem(ProdutoModel produto, Integer quantidade) {
        ItemPedidoModel item = new ItemPedidoModel();
        item.setProduto(produto);
        item.setPrecoUnitario(produto.getPreco());
        item.setQuantidade(quantidade);
        item.setPedido(this);
        item.calcularSubTotal();

        this.itens.add(item);
        this.atualizarValorTotal();
    }

}
