package br.com.carteira.carteira.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record RelatorioMensalDTO(
        String mesAno,
        List<AtivoResponseDTO> ativos,
        BigDecimal totalInvestido,
        BigDecimal totalAtual,
        BigDecimal lucroPrejuizoTotal,
        BigDecimal dividendosNoMes,
        Map<String, BigDecimal> dividendosPorAtivo
) {
}
