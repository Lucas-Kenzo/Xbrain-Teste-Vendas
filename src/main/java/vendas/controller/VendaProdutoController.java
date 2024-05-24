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

    @GetMapping("{vendaId}/produto/{produtoId}")
    public ResponseEntity<VendaProduto> findByVendaIdAndProdutoId(@PathVariable Long vendaId,
                                                                  @PathVariable Long produtoId) {
        var vendaProduto = service.findByVendaIdAndProdutoId(vendaId, produtoId);
        return ResponseEntity.ok(vendaProduto);
    }

}
