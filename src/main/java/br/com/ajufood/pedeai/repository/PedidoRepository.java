package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.PedidoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    Page<PedidoModel> findByClienteId(int clienteId, Pageable pageable);

    Page<PedidoModel> findByClienteIdAndStatusIgnoreCase(int clienteId, String status, Pageable pageable);
}