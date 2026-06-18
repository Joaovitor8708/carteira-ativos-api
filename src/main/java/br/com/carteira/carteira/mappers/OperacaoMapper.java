package br.com.carteira.carteira.mappers;

import br.com.carteira.carteira.dtos.OperacaoImportadaDTO;
import br.com.carteira.carteira.enums.OrigemOperacao;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Operacao;

public class OperacaoMapper {

    public static Operacao toEntity(OperacaoImportadaDTO dto, Ativo ativo){
        Operacao operacao = new Operacao();
        operacao.setAtivo(ativo);
        operacao.setTipo(dto.tipo());
        operacao.setQuantidade(dto.quantidade());
        operacao.setPrecoUnitario(dto.precoUnitario());
        operacao.setValorTotal(dto.valorTotal());
        operacao.setTaxaLiquidacao(dto.taxaLiquidacao());
        operacao.setDataPregao(dto.dataPregao());
        operacao.setNumeroNota(dto.numeroNota());
        operacao.setOrigem(OrigemOperacao.PDF_IMPORTADO);

        return operacao;
    }
}
