package vendas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vendas.exception.NotFoundException;
import vendas.model.VendaProduto;
import vendas.repository.VendaProdutoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendaProdutoService {

    private final VendaProdutoRepository repository;

    private final ProdutoService produtoService;
    
    public List<VendaProduto> findAll(){
        return repository.findAll();
    }

    public VendaProduto findById(Long id){
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException("VendaProduto de ID " + id + "não encontrada!"));
    }
    
    public Optional<VendaProduto> findByVendaIdAndProdutoId(Long vendaId, Long produtoId) {
        return repository.findByVendaIdAndProdutoId(vendaId, produtoId);
    }

    public VendaProduto findVendaProdutoOrThrow(Long vendaId, Long produtoId) {
        return repository.findByVendaIdAndProdutoId(vendaId, produtoId)
                .orElseThrow(() -> new NotFoundException("Produto de ID " + produtoId + " não encontrado na venda " +
                        "de ID " + vendaId));
    }

    public void excluirRegistrosDaVenda(Long vendaId) {
        var vendaProdutos = repository.findByVendaId(vendaId);
        repository.deleteAll(vendaProdutos);
    }

    public void excluir(Long vendaProdutoId) {
        var vendaProduto = findById(vendaProdutoId);
        repository.delete(vendaProduto);
    }


    public void adicionar(List<VendaProduto> itens){
        repository.saveAll(itens);
    }
}
