package vendas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "vendedor_id")
    private Long vendedorId;

    @Column(name = "nome_vendedor")
    private String VendedorNome;

    @JsonIgnore
    @OneToMany(mappedBy = "vendaProdutoPK.venda")
    private Set<VendaProduto> itens = new HashSet<>();

    public BigDecimal getValor(){
        return itens.stream().map(VendaProduto::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
