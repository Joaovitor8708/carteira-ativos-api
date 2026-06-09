package br.com.carteira.carteira.dtos;

import java.util.List;

public record BrapiResponseDTO(
        List<AtivoResultDTO> results,
        String requestedAt
) {
}
