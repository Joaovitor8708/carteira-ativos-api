package br.com.carteira.carteira.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dividendos")
@Getter
@Setter
@NoArgsConstructor
public class Dividendo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "ativo_id")
    private Ativo ativo;

    private BigDecimal valorPorCota;

    private BigDecimal valorTotal;

    private LocalDate dataPagamento;

    private String tipo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
