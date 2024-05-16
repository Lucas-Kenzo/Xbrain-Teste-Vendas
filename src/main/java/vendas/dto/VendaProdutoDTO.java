package vendas.dto;

import lombok.Getter;
import vendas.model.Produto;
import vendas.model.VendaProduto;

import java.math.BigDecimal;

@Getter
public class VendaProdutoDTO {

    private Long id;
    private String descricao;
    private Integer quantidade;
    private BigDecimal subTotal;

    public VendaProdutoDTO(VendaProduto vendaProduto){
        this.id = vendaProduto.getProduto().getId();
        this.descricao = vendaProduto.getProduto().getDescricao();
        this.quantidade = vendaProduto.getQuantidade();
        this.subTotal = vendaProduto.getSubTotal();
    }
}
