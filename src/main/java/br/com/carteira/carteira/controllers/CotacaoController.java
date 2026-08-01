package br.com.carteira.carteira.controllers;

import br.com.carteira.carteira.services.CotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    @GetMapping("/atualizar")
    public ResponseEntity<String> atualizarTodos(){
        cotacaoService.atualizarTodos();
        return ResponseEntity.ok().body("Cotações atualizadas com sucesso");
    }
}
