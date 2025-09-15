package br.com.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        Long accountNumber = 12345L;

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
            throw new AccountNotFoundException(accountNumber);
        });

        assertEquals("Conta de número 12345 não encontrada.", ex.getMessage());
    }
}
