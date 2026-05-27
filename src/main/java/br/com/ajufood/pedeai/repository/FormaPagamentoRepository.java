package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamentoModel, Integer> {
    boolean existsByNome(String nome);
    Optional<FormaPagamentoModel> findByNome(String nome);
}