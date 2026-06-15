package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagamento")
public class PagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message="O valor pago é obrigatório")
    @Column(name="valor_pago", nullable=false, precision=11, scale=2)
    private BigDecimal valorPago;

    @NotNull(message="A data e hora do pagamento são obrigatórias")
    @Column(name="data_hora", nullable=false)
    private LocalDateTime dataHora;

    @NotNull(message="O pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pedido_id", nullable=false)
    private PedidoModel pedido;

    @NotNull(message="A forma de pagamento é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="forma_pagamento_id", nullable=false)
    private FormaPagamentoModel formaPagamento;
}