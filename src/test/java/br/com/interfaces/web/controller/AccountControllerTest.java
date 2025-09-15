package br.com.interfaces.web.controller;

import br.com.application.service.AccountService;
import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AccountController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void operationsShouldAddAttributesIfNotPresent() {
        when(model.containsAttribute("transactionDto")).thenReturn(false);
        when(model.containsAttribute("transferDto")).thenReturn(false);

        String view = controller.operations(model);

        verify(model).addAttribute(eq("transactionDto"), any(TransactionDto.class));
        verify(model).addAttribute(eq("transferDto"), any(TransferDto.class));
        assertEquals("client/operations", view);
    }

    @Test
    void creditShouldRedirectIfBindingHasErrors() {
        TransactionDto dto = new TransactionDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.credit(dto, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.transactionDto", bindingResult);
        verify(redirectAttributes).addFlashAttribute("transactionDto", dto);
        assertEquals("redirect:/client/operations", view);
        verifyNoInteractions(accountService);
    }

    @Test
    void creditShouldCallServiceIfNoErrors() {
        TransactionDto dto = new TransactionDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.credit(dto, bindingResult, redirectAttributes);

        verify(accountService).credit(dto);
        verify(redirectAttributes).addFlashAttribute("message", "Crédito realizado com sucesso");
        assertEquals("redirect:/client/operations", view);
    }

    @Test
    void debitShouldRedirectIfBindingHasErrors() {
        TransactionDto dto = new TransactionDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.debit(dto, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.transactionDto", bindingResult);
        verify(redirectAttributes).addFlashAttribute("transactionDto", dto);
        assertEquals("redirect:/client/operations", view);
        verifyNoInteractions(accountService);
    }

    @Test
    void debitShouldCallServiceIfNoErrors() {
        TransactionDto dto = new TransactionDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.debit(dto, bindingResult, redirectAttributes);

        verify(accountService).debit(dto);
        verify(redirectAttributes).addFlashAttribute("message", "Débito realizado com sucesso");
        assertEquals("redirect:/client/operations", view);
    }

    @Test
    void transferShouldRedirectIfBindingHasErrors() {
        TransferDto dto = new TransferDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.transfer(dto, bindingResult, redirectAttributes, "optimistic");

        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.transferDto", bindingResult);
        verify(redirectAttributes).addFlashAttribute("transferDto", dto);
        assertEquals("redirect:/client/operations", view);
        verifyNoInteractions(accountService);
    }

    @Test
    void transferShouldCallServiceWithOptimistic() {
        TransferDto dto = new TransferDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.transfer(dto, bindingResult, redirectAttributes, "optimistic");

        verify(accountService).transfer(dto, false);
        verify(redirectAttributes).addFlashAttribute("message", "Transferência realizada com sucesso");
        assertEquals("redirect:/client/operations", view);
    }

    @Test
    void transferShouldCallServiceWithPessimistic() {
        TransferDto dto = new TransferDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.transfer(dto, bindingResult, redirectAttributes, "pessimistic");

        verify(accountService).transfer(dto, true);
        verify(redirectAttributes).addFlashAttribute("message", "Transferência realizada com sucesso");
        assertEquals("redirect:/client/operations", view);
    }
}
