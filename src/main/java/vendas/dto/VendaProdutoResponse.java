package vendas.dto;

import lombok.Getter;
import vendas.model.VendaProduto;

import java.math.BigDecimal;

@Getter
public class VendaProdutoResponse {

    private Long id;
    private Long produtoId;
    private Long vendaId;
    private String descricao;
    private BigDecimal valor = BigDecimal.ZERO;
    private Integer quantidade;
    private BigDecimal subTotal = BigDecimal.ZERO;

    public VendaProdutoResponse(VendaProduto vendaProduto){
        this.id = vendaProduto.getId();
        this.vendaId = vendaProduto.getVenda().getId();
        this.produtoId = vendaProduto.getProduto().getId();
        this.descricao = vendaProduto.getProduto().getDescricao();
        this.valor = vendaProduto.getProduto().getValor();
        this.quantidade = vendaProduto.getQuantidade();
        this.subTotal = vendaProduto.getSubTotal();
    }

}
