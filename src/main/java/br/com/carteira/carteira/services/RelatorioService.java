package br.com.carteira.carteira.services;

import br.com.carteira.carteira.dtos.AtivoResponseDTO;
import br.com.carteira.carteira.dtos.RelatorioMensalDTO;
import br.com.carteira.carteira.mappers.AtivoMapper;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Cotacao;
import br.com.carteira.carteira.model.Dividendo;
import br.com.carteira.carteira.repositories.AtivoRepository;
import br.com.carteira.carteira.repositories.CotacaoRepository;
import br.com.carteira.carteira.repositories.DividendoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final AtivoRepository ativoRepository;
    private final CotacaoRepository cotacaoRepository;
    private final DividendoRepository dividendoRepository;

    public RelatorioService(AtivoRepository ativoRepository,
                            CotacaoRepository cotacaoRepository,
                            DividendoRepository dividendoRepository) {
        this.ativoRepository = ativoRepository;
        this.cotacaoRepository = cotacaoRepository;
        this.dividendoRepository = dividendoRepository;
    }

    public RelatorioMensalDTO gerarRelatorioMes(int mes, int ano){
        List<Ativo> ativos = ativoRepository.findAll();
        Map<String, Cotacao> cotacoes = new HashMap<>();

        for (Ativo ativo : ativos){
            cotacaoRepository.findTopByAtivoOrderByDataConsultaDesc(ativo)
                    .ifPresent(ultimaCotacao -> cotacoes.put(ativo.getTicker(), ultimaCotacao));
        }
        List<AtivoResponseDTO> ativosDTO = AtivoMapper.toResponseDTOList(ativos, cotacoes);
        BigDecimal totalInvestido = BigDecimal.ZERO;
        BigDecimal totalAtual = BigDecimal.ZERO;
        BigDecimal lucroPrejuizo = BigDecimal.ZERO;
        for (AtivoResponseDTO ativoResponseDTO : ativosDTO){
            if (ativoResponseDTO.totalInvestido() != null){
                totalInvestido = totalInvestido.add(ativoResponseDTO.totalInvestido());
            }
            if (ativoResponseDTO.totalAtual() != null){
                totalAtual = totalAtual.add(ativoResponseDTO.totalAtual());
            }
            if (ativoResponseDTO.lucroPrejuizo() != null){
                lucroPrejuizo = lucroPrejuizo.add(ativoResponseDTO.lucroPrejuizo());
            }
        }
        YearMonth anoMes = YearMonth.of(ano,mes);
        LocalDate inicio = anoMes.atDay(1);
        LocalDate fim = anoMes.atEndOfMonth();

        List<Dividendo> dividendosMes = dividendoRepository.findByDataPagamentoBetween(inicio, fim);

        BigDecimal valorTotalDividendos = BigDecimal.ZERO;
        Map<String, BigDecimal> dividendosPorAtivo = new HashMap<>();

        for (Dividendo dividendo : dividendosMes){
            valorTotalDividendos = valorTotalDividendos.add(dividendo.getValorTotal());
            String ticker =  dividendo.getAtivo().getTicker();
            BigDecimal valorAcumulado = dividendosPorAtivo.getOrDefault(ticker, BigDecimal.ZERO);
            dividendosPorAtivo.put(ticker, valorAcumulado.add(dividendo.getValorTotal()));
        }
        String mesAnoFormatado = String.format("%02d/%d", mes, ano);
        return new RelatorioMensalDTO(mesAnoFormatado, ativosDTO, totalInvestido, totalAtual, lucroPrejuizo, valorTotalDividendos, dividendosPorAtivo );
    }

}
