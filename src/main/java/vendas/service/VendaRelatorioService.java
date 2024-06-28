package vendas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vendas.dto.VendaRelatorioResponse;
import vendas.exception.ValidacaoException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaRelatorioService {

    private final VendedorService vendedorService;

    private final VendaService vendaService;

    public List<VendaRelatorioResponse> findAll(LocalDate dataInicial, LocalDate dataFinal) {

        if (dataFinal.isBefore(dataInicial)) {
            throw new ValidacaoException("DataFinal não pode ser menor que dataInicial");
        }

        return vendedorService.findAll().stream()
                .map(vendedor -> {
                    var total = vendaService.getVendasFinalizadasPorPeriodo(vendedor.getId(), dataInicial, dataFinal);
                    var relatorio = new VendaRelatorioResponse(vendedor.getNome(), total);
                    relatorio.setMediaDiaria(dataInicial, dataFinal);
                    return relatorio;
                })
                .collect(Collectors.toList());
    }

}
