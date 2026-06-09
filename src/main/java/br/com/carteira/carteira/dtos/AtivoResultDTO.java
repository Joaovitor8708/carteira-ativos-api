package br.com.carteira.carteira.dtos;

import java.math.BigDecimal;

public record AtivoResultDTO(
        String symbol,
        String shortName,
        BigDecimal regularMarketPrice,
        BigDecimal regularMarketChangePercent,
        BigDecimal regularMarketDayHigh,
        BigDecimal regularMarketDayLow,
        BigDecimal regularMarketChange,
        BigDecimal regularMarketPreviousClose,
        BigDecimal regularMarketOpen,
        Long regularMarketVolume,
        String regularMarketTime
) {
}
