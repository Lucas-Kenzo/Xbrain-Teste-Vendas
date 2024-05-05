package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.model.VendaProduto;

public interface VendaProdutoRepository extends JpaRepository<VendaProduto, Long> {
}
