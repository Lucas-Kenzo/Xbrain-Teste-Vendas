package vendas.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static vendas.utils.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    void subtraiQuantidade_quandoSolicitado() {
        var produto = umProduto(1L, BigDecimal.valueOf(100));
        produto.subtraiQuantidade(1);
        assertEquals(1, produto.getQuantidade());
    }

    @Test
    void incrementaQuantidade_quandoSolicitado() {
        var produto = umProduto(1L, BigDecimal.valueOf(100));
        produto.incrementaQuantidade(1);
        assertEquals(3, produto.getQuantidade());
    }
}