package vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vendas.dto.VendaProdutoRequest;
import vendas.enums.ESituacaoVenda;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Produto;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.repository.VendaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private VendaProdutoService vendaProdutoService;

    @Autowired
    private VendedorService vendedorService;
    
    public List<Venda> findAll(){
        return repository.findAll();
    }
    
    public Venda findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venda de ID: " + id + " não encontrada!"));
    }
    
    public Venda criarRascunho(Long vendedorId) {
        var vendedor = vendedorService.findById(vendedorId);
        var venda = new Venda();
        venda.setVendedorId(vendedor.getId());
        venda.setVendedorNome(vendedor.getNome());
        venda.setSituacao(ESituacaoVenda.RASCUNHO);
        return repository.save(venda);
    }

    public Venda salvar(Venda venda){
        return repository.save(venda);
    }

    @Transactional
    public Venda adicionaProdutosNaVenda(Long vendaId, List<VendaProdutoRequest> requests) {
        var venda = findById(vendaId);
        var itens = requests.stream().map(request -> salvarProdutos(venda, request)).toList();
        vendaProdutoService.adicionar(itens);
        venda.setValor();
        venda.setSituacao(ESituacaoVenda.PENDENTE);
        return venda;
    }

    public VendaProduto salvarProdutos(Venda venda, VendaProdutoRequest request) {
        var quantidade = request.quantidade();
        var produto = produtoService.findById(request.produtoId());
        if(produto.getQuantidade() < quantidade){
            throw new ValidacaoException("Produto de ID " + produto.getId() +
                    " não possui quantidade suficiente no estoque");
        }
        produto.subtraiQuantidade(quantidade);
        produtoService.salvar(produto);
        return findByVendaProdutoOrNewVendaProduto(venda, produto, quantidade);

    }

    public Venda finalizarVenda(Long vendaId) {
        var venda = findById(vendaId);

        if(venda.getSituacao().equals(ESituacaoVenda.FINALIZADA)){
            throw new ValidacaoException("A venda de ID " + vendaId +
                    " já está finalizada");
        }

        if(venda.getItens().isEmpty()){
            throw new ValidacaoException("A venda de ID " + vendaId +
                    " não pode ser finalizada, pois não possui itens!");
        }
        venda.setSituacao(ESituacaoVenda.FINALIZADA);
        venda.setDataDaVenda();
        return salvar(venda);
    }

    public VendaProduto findByVendaProdutoOrNewVendaProduto(Venda venda, Produto produto, Integer quantidade){
        var vendaProdutoAtualOpt = vendaProdutoService.findByVendaIdAndProdutoId(venda.getId(), produto.getId());
        return vendaProdutoAtualOpt.map(vendaProdutoAtual -> {
            vendaProdutoAtual.incrementeQuantidade(quantidade);
            vendaProdutoAtual.setSubTotal();
            return vendaProdutoAtual;
        }).orElseGet(() -> new VendaProduto(venda, produto, quantidade));
    }

    public Integer getVendasFinalizadasPorPeriodo(Long vendedorId, LocalDate dataInicial, LocalDate dataFinal) {
        return repository.countByVendedorIdAndSituacaoAndDataDaVendaBetween(vendedorId, ESituacaoVenda.FINALIZADA,
                dataInicial.atStartOfDay(), dataFinal.atTime(LocalTime.MAX));
    }


}
