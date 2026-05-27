package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Integer> {

    List<PagamentoModel> findByPedidoId(int pedidoId);
    List<PagamentoModel> findByFormaPagamentoId(int formaPagamentoId);
    boolean existsByPedidoId(int pedidoId);
}