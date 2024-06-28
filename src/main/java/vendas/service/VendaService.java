package vendas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import vendas.dto.VendaProdutoRequest;
import vendas.enums.ESituacaoVenda;
import vendas.exception.EntidadeEmUsoException;
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
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository repository;

    private final ProdutoService produtoService;

    private final VendaProdutoService vendaProdutoService;

    private final VendedorService vendedorService;

    public List<Venda> findAll() {
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
        return salvar(venda);
    }

    public Venda salvar(Venda venda) {
        return repository.save(venda);
    }

    @Transactional
    public Venda adicionaProdutosNaVenda(Long vendaId, List<VendaProdutoRequest> requests) {
        var venda = findById(vendaId);
        if (venda.getSituacao().equals(ESituacaoVenda.FINALIZADA)) {
            throw new ValidacaoException("Não é possível alterar os produtos de uma venda já finalizada");
        }
        var itens = salvarProdutos(venda, requests);
        comparaItens(venda, itens);
        venda.setItens(itens);
        vendaProdutoService.adicionar(itens);
        venda.calcularValorTotal();
        venda.setSituacao(ESituacaoVenda.PENDENTE);
        return salvar(venda);
    }

    public List<VendaProduto> salvarProdutos(Venda venda, List<VendaProdutoRequest> requests) {
        return requests.stream().map(request -> {
            var quantidade = request.quantidade();
            var produto = produtoService.findById(request.produtoId());
            return findByVendaProdutoOrNewVendaProduto(venda, produto, quantidade);
        }).toList();
    }

    public VendaProduto findByVendaProdutoOrNewVendaProduto(Venda venda, Produto produto, Integer quantidade) {
        var vendaProdutoAtualOpt = vendaProdutoService.findByVendaIdAndProdutoId(venda.getId(), produto.getId());
        return vendaProdutoAtualOpt.map(vendaProdutoAtual -> {
            if (produto.getQuantidade() < (quantidade - vendaProdutoAtual.getQuantidade())) {
                throw new ValidacaoException("Produto de ID " + produto.getId() +
                        " não possui quantidade suficiente no estoque");
            }
            if (vendaProdutoAtual.getQuantidade() > quantidade) {
                produto.incrementaQuantidade(vendaProdutoAtual.getQuantidade() - quantidade);
            } else {
                produto.subtraiQuantidade(quantidade - vendaProdutoAtual.getQuantidade());
            }
            produtoService.salvar(produto);
            vendaProdutoAtual.setQuantidade(quantidade);
            vendaProdutoAtual.setSubTotal();
            return vendaProdutoAtual;
        }).orElseGet(() -> {
            if (produto.getQuantidade() < quantidade) {
                throw new ValidacaoException("Produto de ID " + produto.getId() +
                        " não possui quantidade suficiente no estoque");
            }
            produto.subtraiQuantidade(quantidade);
            produtoService.salvar(produto);
            return new VendaProduto(venda, produto, quantidade);
        });
    }

    public void comparaItens(Venda venda, List<VendaProduto> itens) {
        venda.getItens().forEach(itemAtual -> {
            if (itens.stream().noneMatch(item -> item.getProduto().getId().equals(itemAtual.getProdutoId()))) {
                var produto = produtoService.findById(itemAtual.getProdutoId());
                produto.incrementaQuantidade(itemAtual.getQuantidade());
                produtoService.salvar(produto);
                vendaProdutoService.excluir(itemAtual.getId());
            }
        });
    }

    public Venda finalizarVenda(Long vendaId) {
        var venda = findById(vendaId);
        seVendaFinalizada(vendaId, venda);
        seVendaPossuiItens(vendaId, venda);
        venda.setSituacao(ESituacaoVenda.FINALIZADA);
        venda.setDataDaVenda();
        return salvar(venda);
    }

    private static void seVendaPossuiItens(Long vendaId, Venda venda) {
        if (ObjectUtils.isEmpty(venda.getItens())) {
            throw new ValidacaoException("A venda de ID " + vendaId +
                    " não pode ser finalizada, pois não possui itens!");
        }
    }

    private static void seVendaFinalizada(Long vendaId, Venda venda) {
        if (venda.getSituacao() == ESituacaoVenda.FINALIZADA) {
            throw new ValidacaoException("A venda de ID " + vendaId +
                    " já está finalizada");
        }
    }

    public Integer getVendasFinalizadasPorPeriodo(Long vendedorId, LocalDate dataInicial, LocalDate dataFinal) {
        return repository.countByVendedorIdAndSituacaoAndDataDaVendaBetween(vendedorId, ESituacaoVenda.FINALIZADA,
                dataInicial.atStartOfDay(), dataFinal.atTime(LocalTime.MAX));
    }

    @Transactional
    public Venda alterarVendedor(Long vendaId, Long vendedorId) {

        var venda = findById(vendaId);

        if (venda.getSituacao().equals(ESituacaoVenda.FINALIZADA)) {
            throw new ValidacaoException("Não é possível trocar o vendedor de uma venda finalizada");
        }
        var vendedor = vendedorService.findById(vendedorId);
        venda.setVendedorId(vendedor.getId());
        venda.setVendedorNome(vendedor.getNome());
        return salvar(venda);
    }

    @Transactional
    public void excluir(Long vendaId) {
        var venda = findById(vendaId);
        vendaProdutoService.excluirRegistrosDaVenda(vendaId);
        try {
            repository.delete(venda);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Não foi possível excluir a venda de ID " + vendaId);
        }
    }
}
