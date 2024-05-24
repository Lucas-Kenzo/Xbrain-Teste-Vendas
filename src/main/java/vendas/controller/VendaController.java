package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vendas.dto.VendaProdutoRequest;
import vendas.model.Produto;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.service.VendaService;

import java.util.List;

@RestController
@RequestMapping("api/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    public ResponseEntity<List<Venda>> findAll(){
        var vendas = service.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("{id}")
    public Venda findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping("criar-rascunho")
    @ResponseStatus(HttpStatus.CREATED)
    public Venda salvar(@RequestBody Venda venda){
        return service.criarRascunho(venda);
    }

    @PostMapping("adicionar-produtos/{id}")
    public ResponseEntity<List<VendaProduto>> adicionarProdutos(@PathVariable Long id, @RequestBody List<VendaProdutoRequest> request){
        var itens = service.adicionaProdutosNaVenda(id, request);
        return ResponseEntity.ok(itens);
    }
}
