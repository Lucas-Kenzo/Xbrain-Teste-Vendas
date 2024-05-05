package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vendas.model.Vendedor;
import vendas.service.VendedorService;

import java.util.List;

@RestController
@RequestMapping(value = "/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService service;

    @GetMapping
    public ResponseEntity<List<Vendedor>> findAll(){
        var vendedores = service.findAll();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Vendedor> findById(@PathVariable Long id){
        var vendedor = service.findById(id);
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vendedor salvar(@RequestBody Vendedor vendedor){
        return service.salvar(vendedor);
    }
}
