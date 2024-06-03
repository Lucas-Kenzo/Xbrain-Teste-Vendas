package vendas.utils;

import vendas.dto.VendaProdutoRequest;
import vendas.enums.ECategoriaProduto;
import vendas.enums.ESituacaoVenda;
import vendas.model.Produto;
import vendas.model.Venda;
import vendas.model.VendaProduto;
import vendas.model.Vendedor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {

    public static Venda umaVendaPendente() {
        return Venda.builder()
                .id(1L)
                .valor(BigDecimal.valueOf(360))
                .vendedorId(1L)
                .vendedorNome("VENDEDOR 01")
                .situacao(ESituacaoVenda.PENDENTE)
                .itens(List.of())
                .build();
    }
    public static Venda umaVendaRascunho() {
        return Venda.builder()
                .id(1L)
                .valor(BigDecimal.ZERO)
                .vendedorId(1L)
                .vendedorNome("VENDEDOR 01")
                .situacao(ESituacaoVenda.RASCUNHO)
                .itens(List.of())
                .build();
    }

    public static Venda umaVendaFinalizada() {
        var venda = Venda.builder()
                .id(1L)
                .vendedorId(1L)
                .vendedorNome("VENDEDOR 01")
                .situacao(ESituacaoVenda.FINALIZADA)
                .itens(umalistaVendaProduto())
                .build();
        venda.setValor();
        venda.setDataDaVenda();
        return venda;
    }

    public static Produto umProduto(Long id, BigDecimal valor) {
        return Produto.builder()
                .id(id)
                .descricao("PRODUTO " + id)
                .categoria(ECategoriaProduto.GAMES)
                .valor(valor)
                .quantidade(2)
                .build();
    }

    public static List<VendaProduto> umalistaVendaProduto() {
        var vd1 = new VendaProduto(
                umaVendaPendente(),
                umProduto(1L, BigDecimal.valueOf(120)),
                2
        );
        var vd2 = new VendaProduto(
                umaVendaPendente(),
                umProduto(2L, BigDecimal.valueOf(300)),
                2
        );
        var vd3 = new VendaProduto(
                umaVendaPendente(),
                umProduto(3L, BigDecimal.valueOf(12)),
                3
        );
        return Arrays.asList(vd1, vd2, vd3);
    }

    public static List<VendaProduto> outraListaVendaProduto() {
        var vd1 = new VendaProduto(
                umaVendaPendente(),
                umProduto(4L, BigDecimal.valueOf(230)),
                2
        );
        var vd2 = new VendaProduto(
                umaVendaPendente(),
                umProduto(5L, BigDecimal.valueOf(180)),
                3
        );
        var vd3 = new VendaProduto(
                umaVendaPendente(),
                umProduto(6L, BigDecimal.valueOf(15)),
                2
        );

        vd1.setId(1L);
        vd2.setId(2L);
        vd3.setId(3L);

        return new ArrayList<>(Arrays.asList(vd1, vd2, vd3));
    }

    public static Vendedor umVendedor() {
        return Vendedor.builder()
                .id(1L)
                .nome("VENDEDOR 01")
                .cpf("123.123.123-10")
                .email("vendedor01@gmail.com")
                .build();
    }
    public static List<VendaProdutoRequest> umaListaVendaProdutoRequest() {
        var request1 = new VendaProdutoRequest(1L, 2);
        var request2 = new VendaProdutoRequest(2L, 2);
        var request3 = new VendaProdutoRequest(3L, 2);
        return new ArrayList<>(Arrays.asList(request1, request2, request3));
    }
}
