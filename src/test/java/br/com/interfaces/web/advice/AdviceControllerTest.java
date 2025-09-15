package br.com.interfaces.web.advice;

import br.com.application.exception.AccountNotFoundException;
import br.com.application.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    void handleBusinessExceptionShouldAddFlashAttributeAndRedirect() {
        BusinessException ex = new BusinessException("CODE", "Erro de negócio");

        String result = handler.handleBusinessException(ex, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Erro de negócio");
        assertEquals("redirect:/client/operations", result);
    }

    @Test
    void handleAccountNotFoundShouldAddFlashAttributeAndRedirect() {
        AccountNotFoundException ex = new AccountNotFoundException(123L);

        String result = handler.handleAccountNotFound(ex, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Conta de número 123 não encontrada.");
        assertEquals("redirect:/client/operations", result);
    }

    @Test
    void handleOptimisticLockShouldAddFlashAttributeAndRedirect() {
        String result = handler.handleOptimisticLock(redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Falha por concorrência, tente novamente");
        assertEquals("redirect:/client/operations", result);
    }

    @Test
    void handleIllegalArgShouldAddFlashAttributeAndRedirect() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        String result = handler.handleIllegalArg(ex, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Argumento inválido");
        assertEquals("redirect:/client/operations", result);
    }
}
