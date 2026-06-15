package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {
	List<ProdutoModel> findByDisponivelTrue();
	List<ProdutoModel> findByDisponivelTrueAndCategoria_Id(Integer categoriaId);
}
