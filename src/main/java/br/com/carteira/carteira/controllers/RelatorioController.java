package br.com.carteira.carteira.controllers;

import br.com.carteira.carteira.dtos.RelatorioMensalDTO;
import br.com.carteira.carteira.services.RelatorioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RelatorioController {
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorio")
    public String gerarRelatorioMes(@RequestParam(required = false) Integer mes,
                                    @RequestParam(required = false) Integer ano,
                                     Model model){
        int mesAtual = mes != null ? mes : LocalDate.now().getMonthValue();
        int anoAtual = ano != null ? ano : LocalDate.now().getYear();
        RelatorioMensalDTO relatorioMensalDTO = relatorioService.gerarRelatorioMes(mesAtual,anoAtual);
        model.addAttribute("relatorio", relatorioMensalDTO);
        return "relatorio";
    }
}
