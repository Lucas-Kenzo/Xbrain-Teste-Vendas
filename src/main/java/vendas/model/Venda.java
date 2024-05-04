package vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Date dataDaVenda;

    private BigDecimal valor;

    private Long vendedorId;

    private String nomeVendedor;

    private List<VendaProduto> vendaProdutos;

    public BigDecimal getValor(){
        return vendaProdutos.stream().map(VendaProduto::getSubTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
