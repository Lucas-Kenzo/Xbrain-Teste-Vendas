package vendas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vendas.dto.VendaProdutoRequest;
import vendas.enums.ESituacaoVenda;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Produto;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.model.Vendedor;
import vendas.repository.VendaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static vendas.utils.TestData.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @InjectMocks
    private VendaService service;

    @Mock
    private VendaRepository repository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private VendaProdutoService vendaProdutoService;

    @Mock
    private VendedorService vendedorService;


    @Test
    void findById_deveLancarNotFoundException_quandoVendaNaoEncontrada() {
        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Venda de ID: 1 não encontrada!");

    }

    @Test
    void findById_deveRetornarVenda_quandoSolicitado() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThat(service.findById(1L))
                .extracting(Venda::getId, Venda::getValor, Venda::getSituacao)
                .containsExactlyInAnyOrder(1L, BigDecimal.valueOf(876), ESituacaoVenda.FINALIZADA);

        verify(repository).findById(any(Long.class));
    }

    @Test
    void criarRascunho_deveGerarNovaVendaRascunho_quandoSolicitado() {
        when(vendedorService.findById(1L)).thenReturn(umVendedor());
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));
        assertThat(service.criarRascunho(1L))
                .extracting(Venda::getValor, Venda::getVendedorId, Venda::getVendedorNome, Venda::getSituacao)
                .contains(BigDecimal.ZERO, 1L, "Vendedor 01", ESituacaoVenda.RASCUNHO);

        verify(vendedorService).findById(1L);
        verify(repository).save(any(Venda.class));
    }

    @Test
    void adicionaProdutosNaVenda_DeveLancarValidacaoException_quandoVendaEstiverFinalizada() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThatThrownBy(() -> service.adicionaProdutosNaVenda(1L, any()))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("Não é possível alterar os produtos de uma venda já finalizada");

        verify(repository).findById(anyLong());
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComItem_quandoForNovaVenda() {

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(umaVendaRascunho()));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(0)));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.adicionaProdutosNaVenda(1L,
                Collections.singletonList(new VendaProdutoRequest(1L, 2)));

        assertThat(venda.getItens())
                .extracting("id", "quantidade")
                .containsExactlyInAnyOrder(
                        tuple(null, 2)
                );

        verify(repository).findById(1L);
        verify(repository).save(any(Venda.class));
        verify(produtoService).findById(1L);
        verify(vendaProdutoService).findByVendaIdAndProdutoId(1L, 1L);
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComSituacaoPendente_quandoForNovaVenda() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(umaVendaRascunho()));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(0)));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.adicionaProdutosNaVenda(1L, List.of(umaListaVendaProdutoRequest().get(0))))
                .extracting("id", "valor", "situacao")
                .containsExactlyInAnyOrder(1L, BigDecimal.valueOf(240), ESituacaoVenda.PENDENTE);

        verify(repository).findById(anyLong());
        verify(produtoService).findById(1L);
        verify(vendaProdutoService).findByVendaIdAndProdutoId(1L, 1L);
        verify(repository).save(any(Venda.class));
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComMenosItens_quandoInserirMenosQuantidadeDeItemJaExistente() {
        var vendaAtual = umaVendaPendente();
        var vendaProduto = umalistaVendaProduto().get(0);
        vendaProduto.setQuantidade(10);
        vendaProduto.setSubTotal();
        vendaAtual.setItens(Collections.singletonList((vendaProduto)));
        vendaAtual.calcularValorTotal();

        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(vendaProduto));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.adicionaProdutosNaVenda(1L,
                Collections.singletonList(new VendaProdutoRequest(1L, 2)));

        assertEquals(2, venda.getItens().get(0).getQuantidade());

        verify(repository).findById(anyLong());
        verify(produtoService).findById(1L);
        verify(vendaProdutoService).findByVendaIdAndProdutoId(1L, 1L);
        verify(repository).save(any(Venda.class));

    }

    @Test
    void salvarProdutos_deveRetornarListaVendaProduto_quandoChamado() {
        AtomicLong counter = new AtomicLong(1);
        var produtos = new ArrayList<Produto>();
        when(produtoService.findById(anyLong())).thenAnswer((invocation -> {
            var id = counter.getAndIncrement();
            produtos.add(umProduto(id, BigDecimal.valueOf(100)));
            return umProduto(id, BigDecimal.valueOf(100));
        }));

        assertThat(service.salvarProdutos(umaVendaPendente(), umaListaVendaProdutoRequest()))
                .extracting(VendaProduto::getProduto)
                .containsSequence(produtos);

        verify(produtoService, times(3)).findById(anyLong());
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveCriarNovaVendaProduto_quandoNaoExistente() {
        var item = service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 2);

        var produto = umProduto(1L, BigDecimal.valueOf(100));
        produto.setQuantidade(0);

        assertThat(service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(), umProduto(1L,
                BigDecimal.valueOf(100)), 2))
                .extracting(VendaProduto::getId, VendaProduto::getProduto, VendaProduto::getQuantidade)
                .contains(null, produto, 2);
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveLancarValidacaoException_quandoQtdProdutoForMenorQueSomaTotal() {
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(1)));

        assertThatThrownBy(() -> service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 5))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("Produto de ID 1 não possui quantidade suficiente no estoque");

        verify(vendaProdutoService).findByVendaIdAndProdutoId(1L, 1L);
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveLancarValidacaoException_quandoQtdProdutoForMenorQueSolicitado() {
        assertThatThrownBy(() -> service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 5))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("Produto de ID 1 não possui quantidade suficiente no estoque");
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveSetarQuantidadeItem_quandoItemEncontrado() {
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(1)));

        assertThat(service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 1))
                .extracting(VendaProduto::getQuantidade)
                .isEqualTo(1);

        verify(vendaProdutoService).findByVendaIdAndProdutoId(1L, 1L);
    }

    @Test
    void finalizarVenda_deveLancarValidacaoException_quandoVendaJaFinalizada() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThatThrownBy(() -> service.finalizarVenda(1L))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("A venda de ID 1 já está finalizada");

        verify(repository).findById(anyLong());
    }

    @Test
    void finalizarVenda_deveLancarValidacaoException_quandoVendaRascunhoSemItem() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(umaVendaRascunho()));

        assertThatThrownBy(() -> service.finalizarVenda(1L))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("A venda de ID 1 não pode ser finalizada, pois não possui itens!");

        verify(repository).findById(anyLong());
    }

    @Test
    void finalizarVenda_deveAlterarSituacaoParaFinalizado_quandoSolicitado() {
        var vendaAtual = umaVendaPendente();
        vendaAtual.setItens(umalistaVendaProduto());
        vendaAtual.calcularValorTotal();

        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.finalizarVenda(1L))
                .extracting(Venda::getId, Venda::getSituacao)
                .contains(1L, ESituacaoVenda.FINALIZADA);

        verify(repository).findById(anyLong());
    }

    @Test
    void finalizarVenda_deveSetarDataDataVenda_quandoSolicitado() {
        var horario = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        var vendaAtual = umaVendaPendente();
        vendaAtual.setItens(umalistaVendaProduto());
        vendaAtual.calcularValorTotal();
        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.finalizarVenda(anyLong())).extracting(Venda::getDataDaVenda).isNotEqualTo(null);

        verify(repository).findById(anyLong());

    }

    @Test
    void alterarVendedor_deveAlterarVendedorDaVenda_quandoSolicitado() {
        var novoVendedor = new Vendedor(2L, "123.456.789-90", "Vendedor 02", "test02@gmail.com");
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaPendente()));
        when(vendedorService.findById(anyLong())).thenReturn(novoVendedor);
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.alterarVendedor(1L, 1L))
                .extracting(Venda::getId, Venda::getVendedorId, Venda::getVendedorNome)
                .contains(1L, 2L, "Vendedor 02");

        verify(repository).findById(anyLong());
        verify(vendedorService).findById(anyLong());
    }

    @Test
    void alterarVendedor_deveLancarValidacaoException_quandoVendaFinalizada() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThatThrownBy(() -> service.alterarVendedor(1L, 1L))
                .isInstanceOf(ValidacaoException.class)
                .hasMessage("Não é possível trocar o vendedor de uma venda finalizada");

        verify(repository).findById(anyLong());
    }
}