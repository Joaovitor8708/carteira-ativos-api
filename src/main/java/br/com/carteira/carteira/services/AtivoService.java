package br.com.carteira.carteira.services;

import br.com.carteira.carteira.dtos.AtivoRequestDTO;
import br.com.carteira.carteira.dtos.AtivoResponseDTO;
import br.com.carteira.carteira.dtos.OperacaoImportadaDTO;
import br.com.carteira.carteira.enums.OrigemOperacao;
import br.com.carteira.carteira.enums.TipoOperacao;
import br.com.carteira.carteira.exception.AtivoJaCadastradoException;
import br.com.carteira.carteira.exception.AtivoNotFoundException;
import br.com.carteira.carteira.mappers.AtivoMapper;
import br.com.carteira.carteira.mappers.OperacaoMapper;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Cotacao;
import br.com.carteira.carteira.model.Operacao;
import br.com.carteira.carteira.repositories.AtivoRepository;
import br.com.carteira.carteira.repositories.CotacaoRepository;
import br.com.carteira.carteira.repositories.OperacaoRepository;
import br.com.carteira.carteira.services.parser.NotaCorretagemParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class AtivoService {
    private final AtivoRepository ativoRepository;
    private final OperacaoRepository operacaoRepository;
    private final CotacaoRepository cotacaoRepository;
    private final PrecoMedioService precoMedioService;
    private final NotaCorretagemParser notaCorretagemParser;


    public AtivoService(AtivoRepository ativoRepository,
                        OperacaoRepository operacaoRepository,
                        CotacaoRepository cotacaoRepository,
                        PrecoMedioService precoMedioService,
                        NotaCorretagemParser notaCorretagemParser) {
        this.ativoRepository = ativoRepository;
        this.operacaoRepository = operacaoRepository;
        this.cotacaoRepository = cotacaoRepository;
        this.precoMedioService = precoMedioService;
        this.notaCorretagemParser = notaCorretagemParser;
    }

    public Ativo cadastrarManual(AtivoRequestDTO dto){
        if (ativoRepository.existsByTicker(dto.ticker())){
            throw new AtivoJaCadastradoException("Ativo já cadastrado!");
        }
        BigDecimal totalInvestido = dto.precoMedio().multiply(new BigDecimal(dto.quantidade()));
        Ativo ativo = new Ativo();
        ativo.setTicker(dto.ticker());
        ativo.setQuantidade(dto.quantidade());
        ativo.setPrecoMedio(dto.precoMedio());
        ativo.setTotalInvestido(totalInvestido);
        ativoRepository.save(ativo);

        Operacao operacao = new Operacao();
        operacao.setAtivo(ativo);
        operacao.setTipo(TipoOperacao.COMPRA);
        operacao.setQuantidade(dto.quantidade());
        operacao.setPrecoUnitario(dto.precoMedio());
        operacao.setValorTotal(totalInvestido);
        operacao.setTaxaLiquidacao(BigDecimal.ZERO);
        operacao.setDataPregao(LocalDate.now());
        operacao.setNumeroNota(null);
        operacao.setOrigem(OrigemOperacao.MANUAL);
        operacaoRepository.save(operacao);
        return ativo;
    }

    public List<Ativo> importarNotaPdf(MultipartFile file) throws IOException {
        List<OperacaoImportadaDTO> notas = notaCorretagemParser.parsearPDF(file);
        List<Ativo> ativos = new ArrayList<>();

        for (OperacaoImportadaDTO importadaDTO : notas){
            if(operacaoRepository.existsByNumeroNota(importadaDTO.numeroNota())){
                continue;
            } else {
                Optional<Ativo> ativoOptional = ativoRepository.findByTicker(importadaDTO.ticker());
                    Ativo ativo = ativoOptional.orElseGet(() -> {
                        Ativo ativoNovo = new Ativo();
                        ativoNovo.setTicker(importadaDTO.ticker());
                        return ativoRepository.save(ativoNovo);
                    });
                    Operacao operacao = OperacaoMapper.toEntity(importadaDTO,ativo);
                    operacaoRepository.save(operacao);
                    List<Operacao> compras = operacaoRepository.findByAtivoAndTipo(ativo, TipoOperacao.COMPRA);
                    int quantidadeTotal = compras.stream()
                            .mapToInt(Operacao::getQuantidade)
                            .sum();
                    ativo.setQuantidade(quantidadeTotal);
                    BigDecimal totalAtual = ativo.getTotalInvestido() != null ? ativo.getTotalInvestido() : BigDecimal.ZERO;
                    ativo.setTotalInvestido(totalAtual.add(operacao.getValorTotal()));
                    BigDecimal novoPrecoMedio = precoMedioService.calcular(ativo);
                    ativo.setPrecoMedio(novoPrecoMedio);
                    ativoRepository.save(ativo);
                    ativos.add(ativo);
            }
        }
        return ativos;
    }

    public List<AtivoResponseDTO> listarTodos(){
        List<Ativo> ativos = ativoRepository.findAll();
        Map<String, Cotacao> cotacoes = new HashMap<>();

        for (Ativo ativo : ativos){
            cotacaoRepository.findTopByAtivoOrderByDataConsultaDesc(ativo)
                    .ifPresent(ultimaCotacao -> cotacoes.put(ativo.getTicker(), ultimaCotacao));
        }

        return AtivoMapper.toResponseDTOList(ativos,cotacoes);
    }

    public AtivoResponseDTO buscarPorTicker(String ticker) {
        Ativo ativo = ativoRepository.findByTicker(ticker)
                .orElseThrow(() -> new AtivoNotFoundException("Ativo não encontrado: " + ticker));
        Cotacao ultimaCotacao = cotacaoRepository.findTopByAtivoOrderByDataConsultaDesc(ativo).orElse(null);
        return AtivoMapper.toResponseDTO(ativo, ultimaCotacao);
    }

    public void deletar(String ticker) {
        Ativo ativo = ativoRepository.findByTicker(ticker)
                .orElseThrow(() -> new AtivoNotFoundException("Ativo não encontrado: " + ticker));
        ativoRepository.delete(ativo);
    }
}
