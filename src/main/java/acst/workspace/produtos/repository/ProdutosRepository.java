package acst.workspace.produtos.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import acst.workspace.produtos.model.Produto;

@Repository
public interface ProdutosRepository extends JpaRepository<Produto, Long>{
    List<Produto> findByNomeContainingIgnoreCase(String nome, Sort sort);
    @Override
    List<Produto> findAll(Sort sort);
}
