package vendas.model;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import vendas.dto.VendaProdutoResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static vendas.utils.TestData.*;

class VendaTest {

    @Test
    void substituiItens_deveRetornarSomenteItensNovos_quandoSolicitado() {
        var venda = umaVendaPendente();
        venda.setItens(umalistaVendaProduto());
        venda.substituiItens(outraListaVendaProduto());

        var itensEsperados = outraListaVendaProduto().stream().map(VendaProdutoResponse::new).toList();

        assertThat(venda.getItens())
                .extracting(VendaProdutoResponse::getId, VendaProdutoResponse::getVendaId,
                        VendaProdutoResponse::getProdutoId)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, 1L, 4L),
                        Tuple.tuple(2L, 1L, 5L),
                        Tuple.tuple(3L, 1L, 6L)
                );
    }

    @Test
    void getItens_deveRetornarListaVazia_quandoVendaSemItem() {
        assertThat(umaVendaPendente().getItens()).isEmpty();
    }

    @Test
    void getItens_deveRetornarItemResponse_quandoPossuirItem() {
        var venda = umaVendaPendente();
        venda.setItens(outraListaVendaProduto());

        assertThat(venda.getItens())
                .extracting(VendaProdutoResponse::getId, VendaProdutoResponse::getVendaId,
                        VendaProdutoResponse::getProdutoId)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, 1L, 4L),
                        Tuple.tuple(2L, 1L, 5L),
                        Tuple.tuple(3L, 1L, 6L)
                );
    }

}