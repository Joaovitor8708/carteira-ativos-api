package br.com.carteira.carteira.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ativos")
@Getter
@Setter
@NoArgsConstructor
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticker;

    private String nome;

    private Integer quantidade;

    private BigDecimal precoMedio;

    private BigDecimal totalInvestido;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL)
    private List<Cotacao> cotacoes = new ArrayList<>();

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL)
    private List<Dividendo> dividendos = new ArrayList<>();

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL)
    private List<Operacao> operacoes = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
