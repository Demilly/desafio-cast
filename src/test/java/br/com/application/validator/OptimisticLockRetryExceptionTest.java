package br.com.application.validator;

import br.com.application.exception.OptimisticLockRetryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptimisticLockRetryExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Falha por concorrência";

        OptimisticLockRetryException ex = assertThrows(OptimisticLockRetryException.class, () -> {
            throw new OptimisticLockRetryException(message);
        });

        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Falha por concorrência";
        Throwable cause = new RuntimeException("Causa original");

        OptimisticLockRetryException ex = assertThrows(OptimisticLockRetryException.class, () -> {
            throw new OptimisticLockRetryException(message, cause);
        });

        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
