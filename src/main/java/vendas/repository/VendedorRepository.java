package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.model.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
}
