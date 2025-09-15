package br.com.integration.repository;

import br.com.domain.Account;
import br.com.infrastructure.repository.AccountRepository;
import br.com.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class AccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldInsertAccountSuccessfully() {
        Account account = new Account(123456L, "Teste de repositorio container", BigDecimal.valueOf(1000));

        accountRepository.save(account);

        assert (accountRepository.findByNumber(account.getNumber()).isPresent());
    }
}
