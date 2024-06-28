package vendas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vendas.model.Vendedor;
import vendas.service.VendedorService;

import java.util.List;

@RestController
@RequestMapping("api/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService service;

    @GetMapping
    public List<Vendedor> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Vendedor findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vendedor salvar(@RequestBody Vendedor vendedor) {
        return service.salvar(vendedor);
    }

    @PutMapping("{id}")
    public Vendedor editar(@PathVariable Long id, @RequestBody Vendedor vendedor) {
        return service.editar(id, vendedor);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
