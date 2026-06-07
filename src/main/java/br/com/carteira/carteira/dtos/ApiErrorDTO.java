package br.com.carteira.carteira.dtos;


import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorDTO(
        int status,
        String erro,
        List<String> mensagemErros,
        LocalDateTime timestamp,
        String path
) {
}
