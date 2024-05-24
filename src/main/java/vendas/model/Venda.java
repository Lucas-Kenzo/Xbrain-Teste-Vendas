package vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vendas.dto.VendaProdutoDTO;
import vendas.enums.ESituacaoVenda;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venda")
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "data_da_venda")
    private Date dataDaVenda;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "vendedor_id")
    private Long vendedorId;

    @Column(name = "vendedor_nome")
    private String vendedorNome;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private ESituacaoVenda situacao;

    @OneToMany(mappedBy = "venda", fetch = FetchType.LAZY)
    private List<VendaProduto> itens;


    public List<VendaProdutoDTO> getItens(){
        if(!itens.isEmpty()) {
            return this.itens.stream().map(VendaProdutoDTO::new).collect(Collectors.toList());
        }
        return List.of();
    }

    public void adicionarItem(VendaProduto item){
        this.itens.add(item);
    }

}
