package vendas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vendas.model.Produto;
import vendas.service.ProdutoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/produtos")
public class ProdutoController {

    @Autowired
    private final ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> findAll(){
        var produtos = service.findAll();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id){
        var produto = service.findById(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvar(@RequestBody Produto produto){
        return service.salvar(produto);
    }


    @PutMapping("{id}")
    public Produto editar(@PathVariable Long id, @RequestBody Produto produto){
        return service.editar(id, produto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void excluir(@PathVariable Long id){
        service.excluir(id);
    }

}
