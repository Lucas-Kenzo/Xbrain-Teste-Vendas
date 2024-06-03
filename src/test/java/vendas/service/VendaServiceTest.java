package vendas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vendas.dto.VendaProdutoRequest;
import vendas.enums.ESituacaoVenda;
import vendas.exception.EntidadeEmUsoException;
import vendas.exception.NotFoundException;
import vendas.exception.ValidacaoException;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.model.Vendedor;
import vendas.repository.VendaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findById_DeveRetornarVenda_quandoSolicitado() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaFinalizada()));
        var venda = service.findById(1L);
        assertEquals(Venda.class, venda.getClass());
    }

    @Test
    void criarRascunho_deveGerarNovaVendaRascunho_QuandoSolicitado() {
        when(vendedorService.findById(1L)).thenReturn(umVendedor());
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.criarRascunho(1L);

        assertEquals(ESituacaoVenda.RASCUNHO, venda.getSituacao());
    }

    @Test
    void adicionaProdutosNaVenda_DeveLancarValidacaoException_QuandoVendaEstiverFinalizada() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThrows(ValidacaoException.class, () -> service.adicionaProdutosNaVenda(1L,any()));
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComItem_QuandoForNovaVenda() {

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(umaVendaRascunho()));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(0)));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.adicionaProdutosNaVenda(1L,
                Collections.singletonList(new VendaProdutoRequest(1L, 2)));

        assertEquals(1, venda.getItens().size());
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComSituacaoPendente_QuandoForNovaVenda() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(umaVendaRascunho()));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(0)));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.adicionaProdutosNaVenda(1L,
                Collections.singletonList(new VendaProdutoRequest(1L, 2)));

        assertEquals(ESituacaoVenda.PENDENTE, venda.getSituacao());
    }

    @Test
    void adicionaProdutosNaVenda_deveRetornarVendaComMenosItens_QuandoInserirMenosQuantidadeDeItemJsExistente() {
        var vendaAtual = umaVendaPendente();
        var vendaProduto = umalistaVendaProduto().get(0);
        vendaProduto.setQuantidade(10);
        vendaProduto.setSubTotal();
        vendaAtual.setItens(Collections.singletonList((vendaProduto)));
        vendaAtual.setValor();
        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(produtoService.findById(1L)).thenReturn(umProduto(1L, BigDecimal.valueOf(100)));
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(vendaProduto));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.adicionaProdutosNaVenda(1L,
                Collections.singletonList(new VendaProdutoRequest(1L, 2)));

        assertEquals(2, venda.getItens().get(0).getQuantidade());
    }

    @Test
    void salvarProdutos_deveRetornarListaVendaProduto_QuandoChamado() {
        AtomicLong counter = new AtomicLong(1);
        when(produtoService.findById(anyLong())).thenAnswer((invocation -> {
            var id = counter.getAndIncrement();
            return umProduto(id, BigDecimal.valueOf(100));
        }));

        var itens = service.salvarProdutos(umaVendaPendente(), umaListaVendaProdutoRequest());

        assertEquals(3, itens.size());
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveCriarNovaVendaProduto_quandoNaoExistente() {
        var item = service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 2);
        assertEquals(VendaProduto.class, item.getClass());
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveLancarValidacaoException_quandoQtdProdutoForMenorQueSomaTotal() {
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(1)));
        assertThrows(ValidacaoException.class, () -> service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 5));
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveLancarValidacaoException_quandoQtdProdutoForMenorQueSolicitado() {
        assertThrows(ValidacaoException.class, () -> service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 5));
    }

    @Test
    void findByVendaProdutoOrNewVendaProduto_deveSetarQuantidadeItem_quandoItemEncontrado() {
        when(vendaProdutoService.findByVendaIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.ofNullable(umalistaVendaProduto().get(1)));

        var item = service.findByVendaProdutoOrNewVendaProduto(umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(100)), 1);

        assertEquals(1, item.getQuantidade());
    }

    @Test
    void finalizarVenda_deveLancarValidacaoException_quandoVendaJaFinalizada() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(umaVendaFinalizada()));

        assertThrows(ValidacaoException.class, () -> service.finalizarVenda(anyLong()));
    }

    @Test
    void finalizarVenda_deveLancarValidacaoException_quandoVendaRascunho() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(umaVendaRascunho()));
        assertThrows(ValidacaoException.class, () -> service.finalizarVenda(anyLong()));
    }

    @Test
    void finalizarVenda_deveAlterarSituacaoParaFinalizado_quandoSolicitado() {
        var vendaAtual = umaVendaPendente();
        vendaAtual.setItens(umalistaVendaProduto());
        vendaAtual.setValor();
        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.finalizarVenda(anyLong());

        assertEquals(ESituacaoVenda.FINALIZADA, venda.getSituacao());
    }

    @Test
    void finalizarVenda_deveSetarDataDataVenda_quandoSolicitado() {
        var horario = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        var vendaAtual = umaVendaPendente();
        vendaAtual.setItens(umalistaVendaProduto());
        vendaAtual.setValor();
        when(repository.findById(anyLong())).thenReturn(Optional.of(vendaAtual));
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.finalizarVenda(anyLong());

        assertEquals(horario.toLocalDate(), venda.getDataDaVenda().toLocalDate());
    }

    @Test
    void alterarVendedor_DeveAlterarVendedorDaVenda_quandoSolicitado() {
        var novoVendedor = new Vendedor(2L, "123.456.789-90", "Vendedor 02", "test02@gmail.com");
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaPendente()));
        when(vendedorService.findById(anyLong())).thenReturn(novoVendedor);
        when(repository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var venda = service.alterarVendedor(1L, 2L);

        assertEquals(2L, venda.getVendedorId());
    }

    @Test
    void alterarVendedor_DeveLancarValidacaoException_quandoVendaFinalizada() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(umaVendaFinalizada()));
        assertThrows(ValidacaoException.class, () -> service.alterarVendedor(1L, 1L));
    }
}