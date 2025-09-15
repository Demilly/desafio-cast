package br.com.application.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountNumber) {
        super("Conta de número " + accountNumber + " não encontrada.");
    }
}
