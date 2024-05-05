package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vendas.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
