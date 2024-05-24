package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vendas.dto.VendaProdutoDTO;
import vendas.dto.VendaProdutoRequest;
import vendas.enums.ESituacaoVenda;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.repository.VendaRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private VendaProdutoService vendaProdutoService;
    
    public List<Venda> findAll(){
        return repository.findAll();
    }
    
    public Venda findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venda de ID: " + id + " não encontrada!"));
    }
    
    public Venda criarRascunho(Venda venda) {
        venda.setSituacao(ESituacaoVenda.RASCUNHO);
        venda.setDataDaVenda(new Date());
        return repository.save(venda);
    }

    public Venda salvar(Venda venda){
        return repository.save(venda);
    }

    @Transactional
    public List<VendaProduto> adicionaProdutosNaVenda(Long vendaId, List<VendaProdutoRequest> requests) {
        var venda = findById(vendaId);
        var itens = requests.stream().map(request -> salvarProdutos(venda, request)).toList();
        venda.setValor(venda.getValor().add(calculaTotal(itens)));
        vendaProdutoService.adicionar(itens);
        return itens;
    }

    public VendaProduto salvarProdutos(Venda venda, VendaProdutoRequest request) {
        var produto = produtoService.findById(request.getProdutoId());
        if(produto.getQuantidade() <= request.getQuantidade()){
            throw new ValidacaoException("Produto de ID: " + produto.getId() +
                    "não possui quantidade suficiente escolhida");
        }
        produto.setQuantidade(produto.getQuantidade() - request.getQuantidade());
        produtoService.salvar(produto);
        return new VendaProduto(venda, produto, request.getQuantidade());
    }

    public Venda finalizarVenda(Long vendaId) {
        var venda = findById(vendaId);
        if(!venda.getItens().isEmpty()){
            venda.setSituacao(ESituacaoVenda.FINALIZADA);
        }
        return salvar(venda);
    }

    public BigDecimal calculaTotal(List<VendaProduto> itens) {
        if(!itens.isEmpty()){
            return itens.stream().map(VendaProduto::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }
}
