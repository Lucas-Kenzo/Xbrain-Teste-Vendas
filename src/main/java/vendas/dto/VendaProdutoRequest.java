package vendas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vendas.model.Produto;
import vendas.service.ProdutoService;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class VendaProdutoRequest {

    private Long produtoId;
    private Integer quantidade;

}
