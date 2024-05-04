package vendas.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class VendaProduto {

    @EmbeddedId
    private VendaProdutoPK vendaProdutoPK;
    private Integer quantidade;

    public BigDecimal getSubTotal(){
        return vendaProdutoPK.getVenda().getValor().multiply(BigDecimal.valueOf(quantidade));
    }

}
