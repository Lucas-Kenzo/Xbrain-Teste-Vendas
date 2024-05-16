package vendas.enums;

import lombok.Getter;

@Getter
public enum ESituacaoVenda {

    RASCUNHO,
    AGUARDANDO_PAGAMENTO,
    CANCELADA,
    FINALIZADA;
}
