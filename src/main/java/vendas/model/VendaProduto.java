package vendas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "venda_produto")
@NoArgsConstructor
@AllArgsConstructor
public class VendaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "subtotal")
    private BigDecimal subTotal = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_venda", nullable = false)
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    public VendaProduto(Venda venda, Produto produto, Integer quantidade){
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        setSubTotal(produto, quantidade);
    }

    private void setSubTotal(Produto produto, Integer quantidade) {
        BigDecimal valorProduto = produto.getValor() != null ? produto.getValor() : BigDecimal.ZERO;
        this.subTotal = valorProduto.multiply(BigDecimal.valueOf(quantidade != null ? quantidade : 0));
    }

}
