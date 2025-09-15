package br.com.interfaces.web.controller;

import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.application.service.AccountService;
import br.com.domain.dto.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AdminController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listAccountsShouldAddAccountsToModel() {
        AccountDto acc1 = new AccountDto(123L, "Owner 1", BigDecimal.TEN);
        AccountDto acc2 = new AccountDto(456L, "Owner 2", BigDecimal.valueOf(20));
        List<AccountDto> accounts = Arrays.asList(acc1, acc2);

        when(accountService.findAll()).thenReturn(accounts);

        String view = controller.listAccounts(model);

        verify(accountService).findAll();
        verify(model).addAttribute("accounts", accounts);
        assertEquals("admin/accounts", view);
    }

    @Test
    void newAccountFormShouldAddDtoToModel() {
        String view = controller.newAccountForm(model);

        verify(model).addAttribute(eq("accountCreateDto"), any(AccountCreateDto.class));
        assertEquals("admin/new-account", view);
    }

    @Test
    void createAccountShouldReturnFormViewIfBindingHasErrors() {
        AccountCreateDto dto = new AccountCreateDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.createAccount(dto, bindingResult, model);

        assertEquals("admin/new-account", view);
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccountShouldCallServiceIfNoErrors() {
        AccountCreateDto dto = new AccountCreateDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = controller.createAccount(dto, bindingResult, model);

        verify(accountService).createAccount(dto);
        assertEquals("redirect:/admin/accounts", view);
    }
}
