package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.exception.NotFoundException;
import vendas.model.VendaProduto;
import vendas.repository.VendaProdutoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VendaProdutoService {
    
    @Autowired
    private VendaProdutoRepository repository;

    @Autowired
    private ProdutoService produtoService;

    
    public List<VendaProduto> findAll(){
        return repository.findAll();
    }
    
    public Optional<VendaProduto> findByVendaIdAndProdutoId(Long vendaId, Long produtoId) {
        return repository.findByVendaIdAndProdutoId(vendaId, produtoId);
    }

    public void adicionar(List<VendaProduto> itens){
        repository.saveAll(itens);
    }
}
