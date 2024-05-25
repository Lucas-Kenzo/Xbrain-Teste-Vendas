package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vendas.dto.VendaRelatorioDTO;
import vendas.service.VendaRelatorioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/relatorios")
public class VendaRelatorioController {

    @Autowired
    VendaRelatorioService service;

    @GetMapping
    public List<VendaRelatorioDTO> gerarRelatorioVendas(@RequestParam LocalDate dataInicial,
                                                        @RequestParam LocalDate dataFinal) {
        return service.findAll(dataInicial, dataFinal);
    }
}
