package vendas.model;

import jakarta.persistence.*;
import lombok.*;
import vendas.enums.ECategoriaProduto;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name ="descricao",nullable = false)
    private String descricao;

    @Column(name ="categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECategoriaProduto categoria;

    @Column(name ="quantidade", nullable = false)
    private Integer quantidade;


}
