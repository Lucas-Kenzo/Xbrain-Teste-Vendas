package vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vendas.dto.VendaProdutoDTO;
import vendas.enums.ESituacaoVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private LocalDateTime dataDaVenda;

    @Column(name = "valor")
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(name = "vendedor_id")
    private Long vendedorId;

    @Column(name = "vendedor_nome")
    private String vendedorNome;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private ESituacaoVenda situacao;

    @OneToMany(mappedBy = "venda", fetch = FetchType.LAZY)
    private List<VendaProduto> itens;


    public List<VendaProdutoDTO> getItens() {
        return Optional.ofNullable(itens)
                .map(itens -> itens.stream()
                        .map(VendaProdutoDTO::new)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    public void adicionarItem(VendaProduto item){
        this.itens.add(item);
    }

    public void setDataDaVenda(){
        this.dataDaVenda = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public void setValor(){
        this.valor = itens.stream().map(VendaProduto::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
