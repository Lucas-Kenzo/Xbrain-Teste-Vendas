package vendas.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "venda_produto")
@NoArgsConstructor
@AllArgsConstructor
public class VendaProduto {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private VendaProdutoPK vendaProdutoPK;

    @NonNull
    private Integer quantidade;

    public BigDecimal getSubTotal(){
        return vendaProdutoPK.getProduto().getValor().multiply(BigDecimal.valueOf(quantidade));
    }

}
