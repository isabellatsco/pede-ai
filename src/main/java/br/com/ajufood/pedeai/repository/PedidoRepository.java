package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    Optional<PedidoModel> findById(Integer id);
}
