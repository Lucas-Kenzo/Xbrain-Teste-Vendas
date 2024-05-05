package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
