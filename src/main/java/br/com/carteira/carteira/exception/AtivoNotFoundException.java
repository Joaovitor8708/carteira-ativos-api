package br.com.carteira.carteira.exception;

public class AtivoNotFoundException extends RuntimeException {
    public AtivoNotFoundException(String message) {
        super(message);
    }
}
