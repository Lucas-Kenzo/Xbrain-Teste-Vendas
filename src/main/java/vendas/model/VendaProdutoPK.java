package vendas.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VendaProdutoPK {

    @ManyToOne
    @JoinColumn(name = "fk_venda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "fk_produto")
    private Produto produto;
}
