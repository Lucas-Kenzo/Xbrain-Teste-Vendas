package vendas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vendas.model.Produto;
import vendas.service.ProdutoService;

import java.math.BigDecimal;


public record VendaProdutoRequest(
        Long produtoId,
        Integer quantidade
){};
