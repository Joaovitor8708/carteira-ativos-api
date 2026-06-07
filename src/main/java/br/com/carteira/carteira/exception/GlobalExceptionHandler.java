package br.com.carteira.carteira.exception;

import br.com.carteira.carteira.dtos.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AtivoNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleAtivoNotFound(
            AtivoNotFoundException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "Ativo não encontrado",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro); // 404
    }

    @ExceptionHandler(AtivoJaCadastradoException.class)
    public ResponseEntity<ApiErrorDTO> handleAtivoJaCadastrado(
            AtivoJaCadastradoException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.CONFLICT.value(),
                "Ativo já cadastrado",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro); // 409
    }

    @ExceptionHandler(NotaJaImportadaException.class)
    public ResponseEntity<ApiErrorDTO> handleNotaJaImportada(
            NotaJaImportadaException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.CONFLICT.value(),
                "Nota já importada",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro); // 409
    }

    @ExceptionHandler(PdfParseException.class)
    public ResponseEntity<ApiErrorDTO> handlePdfParse(
            PdfParseException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Erro ao processar PDF",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro); // 422
    }

    @ExceptionHandler(BrapiException.class)
    public ResponseEntity<ApiErrorDTO> handleBrapi(
            BrapiException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.BAD_GATEWAY.value(),
                "Erro na API de cotações",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(erro); // 502
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos",
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro); //  400
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request){

        ApiErrorDTO erro = new ApiErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                List.of(ex.getMessage()),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro); // 500
    }

}
