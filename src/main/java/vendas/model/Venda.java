package vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Date dataDaVenda;

    private BigDecimal valor;

    private Long vendedorId;

    private String nomeVendedor;

    @OneToMany(mappedBy = "vendaProdutoPK.venda")
    private Set<VendaProduto> itens = new HashSet<>();

    public BigDecimal getValor(){
        return itens.stream().map(VendaProduto::getSubTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
