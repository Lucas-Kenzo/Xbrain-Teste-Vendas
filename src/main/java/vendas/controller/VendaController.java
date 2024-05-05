package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vendas.model.Venda;
import vendas.service.VendaService;

import java.util.List;

@RestController
@RequestMapping(value = "/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    public ResponseEntity<List<Venda>> findAll(){
        var vendas = service.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Venda> findById(@PathVariable Long id){
        var venda = service.findById(id);
        return ResponseEntity.ok(venda);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venda salvar(@RequestBody Venda venda){
        return service.salvar(venda);
    }
}
