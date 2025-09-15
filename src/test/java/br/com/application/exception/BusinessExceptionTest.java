package br.com.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void shouldCreateExceptionWithCodeAndMessage() {
        String code = "SALDO_INSUFICIENTE";
        String message = "Saldo insuficiente para débito";

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            throw new BusinessException(code, message);
        });

        assertEquals(code, ex.getCode());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        String code = "ERRO";
        String message = "Erro com causa";
        Throwable cause = new RuntimeException("Causa original");

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            throw new BusinessException(code, message, cause);
        });

        assertEquals(code, ex.getCode());
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
