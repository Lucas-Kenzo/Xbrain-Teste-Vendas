package vendas.dto;

public record VendaProdutoRequest(
        Long produtoId,
        Integer quantidade
){};
