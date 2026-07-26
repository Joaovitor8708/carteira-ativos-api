
package br.com.carteira.carteira.services.parser;

import br.com.carteira.carteira.dtos.OperacaoImportadaDTO;
import br.com.carteira.carteira.enums.TipoOperacao;
import br.com.carteira.carteira.exception.PdfParseException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotaCorretagemParser {

    public List<OperacaoImportadaDTO> parsearPDF(MultipartFile arquivo) throws IOException {
        if(arquivo.isEmpty()){
            throw new PdfParseException("Arquivo Vazio");
        }

        if (!"application/pdf".equalsIgnoreCase(arquivo.getContentType())){
            throw new PdfParseException("O arquivo enviado não é um PDF válido");
        }

        List<OperacaoImportadaDTO> operacoes = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(arquivo.getBytes())){
            PDFTextStripper extrator = new PDFTextStripper();
            int totalPaginas = document.getNumberOfPages();

            for (int pagina = 1; pagina <= totalPaginas; pagina++) {
                extrator.setStartPage(pagina);
                extrator.setEndPage(pagina);
                String textoDaPagina = extrator.getText(document);
                String[] linhas = textoDaPagina.split("\\r?\\n");

                int indiceData = -1;
                int indiceNumeroNota = -1;
                BigDecimal taxaLiquidacao = null;

                for (int i = 0; i < linhas.length; i++) {
                    if (linhas[i].trim().startsWith("Data Pregão")) {
                        if (i + 1 < linhas.length) {
                            indiceData = i + 1;
                        }
                    }
                    if (linhas[i].trim().startsWith("Número da nota")) {
                        if (i + 1 < linhas.length) {
                            indiceNumeroNota = i + 1;
                        }
                    }
                    if (linhas[i].trim().startsWith("Taxa de Liquidação")) {
                        String valorLimpo = linhas[i].replaceAll("[^0-9,-]", "")
                                .replace(",", ".");

                        taxaLiquidacao = new BigDecimal(valorLimpo);
                    }
                    if (indiceData != -1 && indiceNumeroNota != -1 && taxaLiquidacao != null) {
                        break;
                    }
                }

                if (taxaLiquidacao == null) {
                    taxaLiquidacao = BigDecimal.ZERO;
                }
                if (indiceData == -1) {
                    throw new PdfParseException("Não foi possível localizar a data do pregão no PDF.");
                }
                if (indiceNumeroNota == -1) {
                    throw new PdfParseException("Não foi possível localizar o número da nota no PDF.");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataPregao;
                String numeroNota = linhas[indiceNumeroNota].trim();
                try {
                    dataPregao = LocalDate.parse(linhas[indiceData].trim(), formatter);
                } catch (Exception e) {
                    throw new PdfParseException("A data encontrada está num formato inválido: " + linhas[indiceData]);
                }
                for (String linha : linhas) {
                    TipoOperacao tipoOperacao;
                    if (linha.startsWith("BOVESPA")) {
                        String[] partes = linha.split(" ");

                        if (partes[1].equalsIgnoreCase("C")) { // "C" ou "V"
                            tipoOperacao = TipoOperacao.COMPRA;
                        } else {
                            tipoOperacao = TipoOperacao.VENDA;
                        }

                        String ticker = partes[3];
                        Integer quantidade = Integer.valueOf(partes[partes.length - 6].trim());
                        BigDecimal precoUnitario = new BigDecimal(partes[partes.length - 4].trim()
                                .replace(".", "")
                                .replace(",", "."));
                        BigDecimal valorTotal = new BigDecimal(partes[partes.length - 2].trim()
                                .replace(".", "")
                                .replace(",", "."));
                        OperacaoImportadaDTO operacaoImportada = new OperacaoImportadaDTO(ticker, tipoOperacao, quantidade, precoUnitario, valorTotal, taxaLiquidacao, dataPregao, numeroNota);
                        operacoes.add(operacaoImportada);
                    }
                }
            }
        } catch (IOException e) {
            throw new PdfParseException("Erro ao ler o arquivo PDF: " + e.getMessage());
        }
        return operacoes;
    }
}
