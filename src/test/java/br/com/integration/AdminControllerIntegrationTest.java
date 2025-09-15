package br.com.integration;

import br.com.application.service.AccountService;
import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import br.com.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerIntegrationTest extends AbstractIntegrationTest {

    @MockBean
    private AccountService accountService;

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void shouldListAccounts() throws Exception {
        when(accountService.findAll()).thenReturn(List.of(
                new AccountDto(1L, "123", null),
                new AccountDto(2L, "456", null)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/accounts"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accounts"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(model().attribute("accounts", hasSize(2)));

        verify(accountService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void shouldShowNewAccountForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/accounts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new-account"))
                .andExpect(model().attributeExists("accountCreateDto"));
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void shouldCreateAccountSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/accounts")
                        .param("number", "789")
                        .param("ownerName", "Charlie Brow Junior")
                        .param("balance", "100")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/accounts"));

        verify(accountService, times(1)).createAccount(any(AccountCreateDto.class));
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    void shouldReturnFormWhenValidationFails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/accounts")
                        .param("number", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new-account"));

        verify(accountService, times(0)).createAccount(any(AccountCreateDto.class));
    }
}

