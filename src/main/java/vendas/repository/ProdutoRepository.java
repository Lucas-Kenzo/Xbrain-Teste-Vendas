package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
