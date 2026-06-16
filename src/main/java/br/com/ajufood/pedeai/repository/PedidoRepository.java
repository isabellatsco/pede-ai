package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.PedidoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    Page<PedidoModel> findByClienteId(int clienteId, Pageable pageable);

    Page<PedidoModel> findByClienteIdAndStatus(int clienteId, String status, Pageable pageable);
}