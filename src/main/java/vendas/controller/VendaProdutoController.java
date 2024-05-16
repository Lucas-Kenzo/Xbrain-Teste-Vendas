package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vendas.model.VendaProduto;
import vendas.service.VendaProdutoService;

import java.util.List;

@RestController
@RequestMapping("api/vendaProdutos")
public class VendaProdutoController {

    @Autowired
    private VendaProdutoService service;

    @GetMapping
    public ResponseEntity<List<VendaProduto>> findAll(){
        var vendaProdutos = service.findAll();
        return ResponseEntity.ok(vendaProdutos);
    }

    @GetMapping("{id}")
    public ResponseEntity<VendaProduto> findById(@PathVariable Long id){
        var vendaProduto = service.findById(id);
        return ResponseEntity.ok(vendaProduto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaProduto salvar(@RequestBody VendaProduto vendaProduto){
        return service.salvar(vendaProduto);
    }
}
