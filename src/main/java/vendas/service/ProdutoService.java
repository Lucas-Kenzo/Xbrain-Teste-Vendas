package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return repository.findById(id).get();
    }
    
    public Produto salvar(Produto Produto){
        return repository.save(Produto);
    }
}
