package br.com.carteira.carteira.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotacoes")
@Getter
@Setter
@NoArgsConstructor
public class Cotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ativo_id")
    private Ativo ativo;

    private BigDecimal precoAtual;

    private BigDecimal abertura;

    private BigDecimal fechamentoAnterior;

    private BigDecimal maxima;

    private BigDecimal minima;

    private BigDecimal variacaoNominal;

    private BigDecimal variacaoPercentual;

    private Long volume;

    private LocalDateTime horaMercado;

    private LocalDate dataConsulta;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

}
