package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vendas.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
}
