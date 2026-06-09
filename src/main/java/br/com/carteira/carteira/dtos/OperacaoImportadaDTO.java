package br.com.carteira.carteira.dtos;

import br.com.carteira.carteira.enums.TipoOperacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperacaoImportadaDTO(
        String ticker,
        TipoOperacao tipo,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotal,
        BigDecimal taxaLiquidacao,
        LocalDate dataPregao,
        String numeroNota
) {
}
