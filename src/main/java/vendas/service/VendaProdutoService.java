package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.model.VendaProduto;
import vendas.repository.VendaProdutoRepository;

import java.util.List;

@Service
public class VendaProdutoService {
    
    @Autowired
    private VendaProdutoRepository repository;
    
    public List<VendaProduto> findAll(){
        return repository.findAll();
    }
    
    public VendaProduto findById(Long id){
        return repository.findById(id).get();
    }
    
    public VendaProduto salvar(VendaProduto VendaProduto){
        return repository.save(VendaProduto);
    }
}
