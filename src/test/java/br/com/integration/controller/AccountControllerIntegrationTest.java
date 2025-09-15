package br.com.integration.controller;

import br.com.application.service.AccountService;
import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;
import br.com.fixture.TransactionDtoFixture;
import br.com.fixture.TransferDtoFixture;
import br.com.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static java.lang.String.valueOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "CLIENT", roles = {"CLIENT"})
class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    @MockBean
    private AccountService accountService;

    @Test
    void shouldDisplayOperationsPageWithEmptyDTOs() throws Exception {
        mockMvc.perform(get("/client/operations").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("client/operations"))
                .andExpect(model().attributeExists("transactionDto"))
                .andExpect(model().attributeExists("transferDto"));
    }

    @Test
    void creditShouldRedirectWithSuccessMessage() throws Exception {
        TransactionDto dto = TransactionDtoFixture.transactionDto();

        mockMvc.perform(post("/client/credit")
                        .param("accountNumber", valueOf(dto.getAccountNumber()))
                        .param("amount", dto.getAmount().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/operations"))
                .andExpect(flash().attribute("message", "Crédito realizado com sucesso"));

        verify(accountService).credit(any(TransactionDto.class));
    }

    @Test
    void debitShouldRedirectWithSuccessMessage() throws Exception {
        TransactionDto dto = TransactionDtoFixture.transactionDto();

        mockMvc.perform(post("/client/debit")
                        .param("accountNumber", valueOf(dto.getAccountNumber()))
                        .param("amount", dto.getAmount().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/operations"))
                .andExpect(flash().attribute("message", "Débito realizado com sucesso"));

        verify(accountService).debit(any(TransactionDto.class));
    }


    @Test
    @WithMockUser(username = "CLIENT", roles = {"CLIENT"})
    void transferShouldRedirectWithOptimisticStrategy() throws Exception {
        TransferDto dto = TransferDtoFixture.transferDto();

        mockMvc.perform(post("/client/transfer")
                        .param("fromAccount", valueOf(dto.getFromAccount()))
                        .param("toAccount", valueOf(dto.getToAccount()))
                        .param("amount", dto.getAmount().toString())
                        .param("strategy", "optimistic")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/operations"))
                .andExpect(flash().attribute("message", "Transferência realizada com sucesso"));

        verify(accountService).transfer(any(TransferDto.class), eq(false));
    }

    @Test
    void transferShouldRedirectWithPessimisticStrategy() throws Exception {
        TransferDto dto = TransferDtoFixture.transferDto();

        mockMvc.perform(post("/client/transfer")
                        .param("fromAccount", valueOf(dto.getFromAccount()))
                        .param("toAccount", valueOf(dto.getToAccount()))
                        .param("amount", dto.getAmount().toString())
                        .param("strategy", "pessimistic"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/operations"))
                .andExpect(flash().attribute("message", "Transferência realizada com sucesso"));

        verify(accountService).transfer(any(TransferDto.class), eq(true));
    }

    @Test
    void creditShouldRedirectWithErrorsWhenInvalid() throws Exception {
        mockMvc.perform(post("/client/credit")
                        .param("accountNumber", "")
                        .param("amount", "-100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/operations"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.transactionDto"))
                .andExpect(flash().attributeExists("transactionDto"));

        verifyNoInteractions(accountService);
    }
}
