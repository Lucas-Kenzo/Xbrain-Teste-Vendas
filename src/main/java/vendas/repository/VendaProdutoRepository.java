package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vendas.model.VendaProduto;

@Repository
public interface VendaProdutoRepository extends JpaRepository<VendaProduto, Long> {
}
