package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vendas.model.VendaProduto;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendaProdutoRepository extends JpaRepository<VendaProduto, Long> {

    Optional<VendaProduto> findByVendaIdAndProdutoId(Long vendaId, Long produtoId);

    List<VendaProduto> findByVendaId(Long vendaId);

}
