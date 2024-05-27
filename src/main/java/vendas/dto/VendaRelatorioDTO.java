package vendas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
@Setter
public class VendaRelatorioDTO {

    private String vendedorNome;
    private Integer totalVendas;
    private BigDecimal mediaDiaria;

    public VendaRelatorioDTO(String vendedorNome, Integer totalVendas){
        this.vendedorNome = vendedorNome;
        this.totalVendas = totalVendas;
    }

    public void setMediaDiaria(LocalDate dataInicial, LocalDate dataFinal){
        int dias = Math.toIntExact(ChronoUnit.DAYS.between(dataInicial, dataFinal)) + 1;
        this.mediaDiaria = totalVendas > 0 && dias > 0 ? BigDecimal.valueOf(totalVendas)
                .divide(BigDecimal.valueOf(dias), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}


