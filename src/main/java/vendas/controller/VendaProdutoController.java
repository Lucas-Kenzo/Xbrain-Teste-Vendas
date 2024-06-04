package vendas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vendas.model.VendaProduto;
import vendas.service.VendaProdutoService;

import java.util.List;

@RestController
@RequestMapping("api/vendaProdutos")
@RequiredArgsConstructor
public class VendaProdutoController {

    private final VendaProdutoService service;

    @GetMapping
    public ResponseEntity<List<VendaProduto>> findAll(){
        var vendaProdutos = service.findAll();
        return ResponseEntity.ok(vendaProdutos);
    }

}
