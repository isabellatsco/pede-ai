package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.CategoriaProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProdutoModel, Integer> {
    boolean existsByNome(String nome);
    Optional<CategoriaProdutoModel> findByNome(String cpf);

}
