package vendas.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "venda_produto")
public class VendaProduto {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private VendaProdutoPK vendaProdutoPK;

    @NonNull
    private Integer quantidade;

    public BigDecimal getSubTotal(){
        return vendaProdutoPK.getVenda().getValor().multiply(BigDecimal.valueOf(quantidade));
    }

}
