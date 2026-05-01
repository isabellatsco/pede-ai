package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
}
