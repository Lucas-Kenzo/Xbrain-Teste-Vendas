package vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vendas.enums.ESituacaoVenda;
import vendas.model.Venda;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    Integer countByVendedorIdAndSituacaoAndDataDaVendaBetween(Long vendedorId, ESituacaoVenda situacao,
                                                              LocalDateTime dataInicial, LocalDateTime dataFinal);
}
