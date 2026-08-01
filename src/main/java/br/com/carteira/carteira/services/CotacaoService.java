package br.com.carteira.carteira.services;

import br.com.carteira.carteira.config.BrapiConfig;
import br.com.carteira.carteira.dtos.AtivoResultDTO;
import br.com.carteira.carteira.dtos.BrapiResponseDTO;
import br.com.carteira.carteira.exception.BrapiException;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Cotacao;
import br.com.carteira.carteira.repositories.AtivoRepository;
import br.com.carteira.carteira.repositories.CotacaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CotacaoService {

    private final BrapiConfig brapiConfig;
    private final RestTemplate restTemplate;
    private final AtivoRepository ativoRepository;
    private final CotacaoRepository cotacaoRepository;

    public CotacaoService(BrapiConfig brapiConfig, RestTemplate restTemplate, AtivoRepository ativoRepository, CotacaoRepository cotacaoRepository) {
        this.brapiConfig = brapiConfig;
        this.restTemplate = restTemplate;
        this.ativoRepository = ativoRepository;
        this.cotacaoRepository = cotacaoRepository;
    }

    public BrapiResponseDTO buscarCotacao(String ticker){
        String url = brapiConfig.getUrl() + "/quote/" + ticker + "?token=" + brapiConfig.getToken();
        BrapiResponseDTO response = restTemplate.getForObject(url, BrapiResponseDTO.class);

        if (response == null){
            throw new BrapiException("Erro ao conectar com a BRAPI");
        }
        if (response.results().isEmpty()){
            throw new BrapiException("ticker não encontrado!");
        }
        return response;
    }

    public void salvarSnapshot(Ativo ativo, BrapiResponseDTO responseDTO){
        AtivoResultDTO dados = responseDTO.results().get(0);
        if (!cotacaoRepository.existsByAtivoAndDataConsulta(ativo, LocalDate.now())){
            LocalDateTime horaMercado = Instant.parse(dados.regularMarketTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Cotacao cotacao = new Cotacao();
            cotacao.setAtivo(ativo);
            cotacao.setPrecoAtual(dados.regularMarketPrice());
            cotacao.setAbertura(dados.regularMarketOpen());
            cotacao.setFechamentoAnterior(dados.regularMarketPreviousClose());
            cotacao.setMaxima(dados.regularMarketDayHigh());
            cotacao.setMinima(dados.regularMarketDayLow());
            cotacao.setVariacaoNominal(dados.regularMarketChange());
            cotacao.setVariacaoPercentual(dados.regularMarketChangePercent());
            cotacao.setVolume(dados.regularMarketVolume());
            cotacao.setHoraMercado(horaMercado);
            cotacao.setDataConsulta(LocalDate.now());
            cotacaoRepository.save(cotacao);
        }
    }

    @Scheduled(cron = "0 0/30 10-17 * * MON-FRI")
    public void atualizarTodos(){
        List<Ativo> ativos = ativoRepository.findAll();

        for (Ativo ativo : ativos){
            BrapiResponseDTO cotacaoBrapi = buscarCotacao(ativo.getTicker());
            salvarSnapshot(ativo, cotacaoBrapi);
        }
    }
}
