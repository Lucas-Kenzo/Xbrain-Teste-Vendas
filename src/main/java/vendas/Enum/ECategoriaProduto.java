package vendas.Enum;

import lombok.Getter;

@Getter
public enum ECategoriaProduto {
    GAMES("Games"),
    CELULARES("Celulares"),
    PERIFERICOS("Periféricos"),
    COMPUTADORES("Computadores"),
    HARDWARE("Hardware"),
    OUTROS("outros");

    private String descricao;

    ECategoriaProduto(String descricao){
        this.descricao = descricao;
    }

}
