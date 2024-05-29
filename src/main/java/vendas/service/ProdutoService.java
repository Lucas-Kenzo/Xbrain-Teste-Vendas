package vendas.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import vendas.exception.EntidadeEmUsoException;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
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

    public Produto gerenciarQuantidade(Long produtoId, Integer quantidade) {
        var produto = findById(produtoId);
        if (quantidade < 0){
            throw new ValidacaoException("Quantidade de produto tem que ser maior que 0");
        }
        produto.setQuantidade(quantidade);
        return salvar(produto);
    }
    public Produto editar(Long produtoId, Produto produto){
        var produtoAtual = findById(produtoId);
        BeanUtils.copyProperties(produto, produtoAtual, "id", "quantidade");
        return salvar(produtoAtual);
    }

    public void excluir(Long produtoId){
        var produto = findById(produtoId);
        try{
            repository.delete(produto);
        }catch(DataIntegrityViolationException e) {
        throw new EntidadeEmUsoException("Não foi possível excluir o produto de ID " + produtoId);
        }
    }
}
