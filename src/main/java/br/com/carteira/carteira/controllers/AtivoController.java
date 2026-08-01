package br.com.carteira.carteira.controllers;

import br.com.carteira.carteira.dtos.AtivoRequestDTO;
import br.com.carteira.carteira.dtos.AtivoResponseDTO;
import br.com.carteira.carteira.services.AtivoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AtivoController {
    private final AtivoService ativoService;


    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    @GetMapping("/carteira")
    public String listarTodos(Model model){
        List<AtivoResponseDTO> ativos = ativoService.listarTodos();
        model.addAttribute("ativos", ativos);
        return "carteira"; // o spring vai procurar o arquivo carteira.html
    }

    @PostMapping("/ativos")
    public String cadastrarManual(@Valid @ModelAttribute AtivoRequestDTO dto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erro", result.getAllErrors());
            return "redirect:/carteira";
        }
        ativoService.cadastrarManual(dto);
        return "redirect:/carteira";
    }

    @PostMapping("/ativos/importar")
    public String importarPdf(@RequestParam("arquivo") MultipartFile arquivo,
                              RedirectAttributes redirectAttributes) throws IOException {
        ativoService.importarNotaPdf(arquivo);
        return "redirect:/carteira";
    }

    @ResponseBody
    @DeleteMapping("/ativos/{ticker}")
    public ResponseEntity<Void> deletar(@PathVariable String ticker) {
        ativoService.deletar(ticker);
        return ResponseEntity.noContent().build();
    }
}
