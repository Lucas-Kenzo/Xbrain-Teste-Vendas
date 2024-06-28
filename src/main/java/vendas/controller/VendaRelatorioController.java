package vendas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vendas.dto.VendaRelatorioResponse;
import vendas.service.VendaRelatorioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/relatorios")
public class VendaRelatorioController {

    public final VendaRelatorioService service;

    @GetMapping
    public List<VendaRelatorioResponse> gerarRelatorioVendas(@RequestParam LocalDate dataInicial,
                                                             @RequestParam LocalDate dataFinal) {
        return service.findAll(dataInicial, dataFinal);
    }
}
