package br.com.carteira.carteira.model;
import br.com.carteira.carteira.enums.OrigemOperacao;
import br.com.carteira.carteira.enums.TipoOperacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "operacoes")
@Getter
@Setter
@NoArgsConstructor
public class Operacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "ativo_id")
    private Ativo ativo;

    @Enumerated(EnumType.STRING)
    private TipoOperacao tipo;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal valorTotal;

    private BigDecimal taxaLiquidacao;

    private LocalDate dataPregao;

    @Column(unique = true)
    private String numeroNota;

    @Enumerated(EnumType.STRING)
    private OrigemOperacao origem;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
