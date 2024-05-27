package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.dto.VendaRelatorioDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaRelatorioService {

    @Autowired
    VendedorService vendedorService;
    @Autowired
    VendaService vendaService;

    public List<VendaRelatorioDTO> findAll(LocalDate dataInicial, LocalDate dataFinal){
        return vendedorService.findAll().stream()
                .map(vendedor -> {
                    var total = vendaService.getVendasFinalizadasPorPeriodo(vendedor.getId(), dataInicial, dataFinal);
                    var relatorio = new VendaRelatorioDTO(vendedor.getNome(), total);
                    relatorio.setMediaDiaria(dataInicial, dataFinal);
                    return relatorio;
                })
                .collect(Collectors.toList());
    }

}
