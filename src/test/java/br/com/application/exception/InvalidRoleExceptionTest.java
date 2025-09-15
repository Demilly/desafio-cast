package br.com.application.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidRoleExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Função inválida para o usuário";

        InvalidRoleException ex = assertThrows(InvalidRoleException.class, () -> {
            throw new InvalidRoleException(message);
        });

        assertEquals(message, ex.getMessage());
    }
}
