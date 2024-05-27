package vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
