package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.exception.NotFoundException;
import vendas.model.Produto;
import vendas.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;
    
    public List<Produto> findAll(){
        return repository.findAll();
    }
    
    public Produto findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new  NotFoundException("Produto de ID: " + id + " não encontrado!"));
    }
    
    public Produto salvar(Produto Produto){
        return repository.save(Produto);
    }

    public Produto removerProdutosVendidos(Produto produto, Integer quantidade){
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        return salvar(produto);
    }
}
