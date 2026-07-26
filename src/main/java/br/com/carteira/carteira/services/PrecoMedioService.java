package br.com.carteira.carteira.services;

import br.com.carteira.carteira.enums.TipoOperacao;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Operacao;
import br.com.carteira.carteira.repositories.OperacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PrecoMedioService {

    private final OperacaoRepository operacaoRepository;

    public PrecoMedioService(OperacaoRepository operacaoRepository){
        this.operacaoRepository = operacaoRepository;
    }

    public BigDecimal calcular(Ativo ativo){
        List<Operacao> compras = operacaoRepository.findByAtivoAndTipo(ativo, TipoOperacao.COMPRA);
        BigDecimal valorTotal = BigDecimal.ZERO;
        Integer quantidadeTotal = 0;

        for (Operacao operacao : compras){
            valorTotal = valorTotal.add(operacao.getPrecoUnitario().multiply(BigDecimal.valueOf(operacao.getQuantidade())));
            quantidadeTotal += operacao.getQuantidade();
        }

        return valorTotal.divide(BigDecimal.valueOf(quantidadeTotal),6, RoundingMode.HALF_UP);

    }
}
