package vendas.model;

import org.junit.jupiter.api.Test;
import vendas.dto.VendaProdutoResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static vendas.utils.TestData.*;

class VendaTest {

    @Test
    void substituiItens_deveRetornarSomenteItensNovos_quandoSolicitado() {
        var venda = umaVendaPendente();
        venda.setItens(umalistaVendaProduto());
        venda.substituiItens(outraListaVendaProduto());

        var itensEsperados = outraListaVendaProduto().stream().map(VendaProdutoResponse::new).toList();

        assertTrue(verificaSeTodosItensIguais(itensEsperados, venda.getItens()));
    }

    private boolean verificaSeTodosItensIguais(List<VendaProdutoResponse> e, List<VendaProdutoResponse> a) {
       return e.stream().allMatch(itemEsperado ->
               a.stream().anyMatch(itemAtual -> itemAtual.getId().equals(itemEsperado.getId())));
    }
}