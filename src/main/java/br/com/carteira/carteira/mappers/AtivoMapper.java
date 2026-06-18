package br.com.carteira.carteira.mappers;

import br.com.carteira.carteira.dtos.AtivoResponseDTO;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Cotacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtivoMapper {

    public static AtivoResponseDTO toResponseDTO(Ativo ativo, Cotacao ultimaCotacao){
        BigDecimal precoAtual = (ultimaCotacao != null) ? ultimaCotacao.getPrecoAtual() : null;
        BigDecimal totalInvestido = ativo.getPrecoMedio().multiply(BigDecimal.valueOf(ativo.getQuantidade()));
        BigDecimal variacao = null;
        BigDecimal totalAtual = null;
        BigDecimal lucroPrejuizo = null;
        if (precoAtual != null){
             variacao = ((precoAtual.divide(ativo.getPrecoMedio(), 6, RoundingMode.HALF_UP))
                            .subtract(BigDecimal.ONE))
                            .multiply(BigDecimal.valueOf(100));
            totalAtual = precoAtual.multiply(BigDecimal.valueOf(ativo.getQuantidade()));
            lucroPrejuizo = totalAtual.subtract(totalInvestido);
        }

        return new AtivoResponseDTO(
                ativo.getTicker(),
                ativo.getNome(),
                ativo.getQuantidade(),
                ativo.getPrecoMedio(),
                precoAtual,
                variacao,
                totalInvestido,
                totalAtual,
                lucroPrejuizo
        );
    }
    public static List<AtivoResponseDTO> toResponseDTOList(List<Ativo> ativos, Map<String, Cotacao> cotacoes){
        List<AtivoResponseDTO> resultado = new ArrayList<>();
        for (Ativo ativo : ativos){
            Cotacao cotacao = cotacoes.get(ativo.getTicker());
            AtivoResponseDTO responseDTO = toResponseDTO(ativo,cotacao);
            resultado.add(responseDTO);
        }
        return resultado;
    }

}
