package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {

    Optional<ClienteModel> findByCpf(String cpf);

    Optional<ClienteModel> findByEmail(String email);

    List<ClienteModel> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query(value = """
           SELECT c
           FROM ClienteModel c
           WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
           """)
    List<ClienteModel> buscarPorNomeJpql(String nome);

    @Query(value = """
           SELECT c
           FROM ClienteModel c
           WHERE c.cpf = :cpf
           """)
    Optional<ClienteModel> buscarPorCpfJpql(String cpf);

    @Query(value = """
           SELECT c
           FROM ClienteModel c
           WHERE c.telefone = :telefone
           """)
    List<ClienteModel> buscarPorTelefoneJpql(String telefone);

    @Query(value = """
           SELECT *
           FROM cliente
           ORDER BY nome
           """, nativeQuery = true)
    List<ClienteModel> buscarTodosSqlNativo();

    @Query(value = """
           SELECT *
           FROM cliente
           WHERE cpf = :cpf
           ORDER BY nome
           """, nativeQuery = true)
    Optional<ClienteModel> buscarPorCpfSqlNativo(String cpf);

    @Query(value = """
           SELECT *
           FROM cliente
           WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))
           ORDER BY nome
           """, nativeQuery = true)
    List<ClienteModel> buscarPorNomeSqlNativo(String nome);

    @Query(value = """
           SELECT COUNT(*)
           FROM cliente
           """, nativeQuery = true)
    Long contarClientesSqlNativo();
}