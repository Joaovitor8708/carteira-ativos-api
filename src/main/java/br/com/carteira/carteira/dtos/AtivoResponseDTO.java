package br.com.carteira.carteira.dtos;

import java.math.BigDecimal;

public record AtivoResponseDTO(
        String ticker,
        String nome,
        Integer quantidade,
        BigDecimal precoMedio,
        BigDecimal precoAtual,
        BigDecimal variacao,
        BigDecimal totalInvestido,
        BigDecimal totalAtual,
        BigDecimal lucroPrejuizo
) {
}

