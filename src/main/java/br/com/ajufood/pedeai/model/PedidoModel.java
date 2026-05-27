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
    @Column(name="dataHora", nullable=false)
    private LocalDateTime dataHora;

    @NotBlank(message="O status é obrigatório")
    @Length(max=128)
    @Column(name="status", nullable=false, length=128)
    private String status;

    @NotNull(message="O valor total é obrigatório")
    @Column(name="valorTotal", nullable=false, precision=11, scale=2)
    private BigDecimal valorTotal;

    @NotNull(message="O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name="clienteID", nullable=false)
    private ClienteModel cliente;

    @NotNull(message="O endereço de entrega é obrigatório")
    @ManyToOne
    @JoinColumn(name="enderecoEntregaID", nullable=false)
    private EnderecoModel enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoModel> itens = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagamentoModel> pagamentos = new ArrayList<>();
}