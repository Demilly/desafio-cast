package br.com.infrastructure.repository;

import br.com.domain.Account;
import br.com.infrastructure.jpa.JpaAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AccountRepositoryTest {

    @Mock
    private JpaAccountRepository jpaAccountRepository;

    @InjectMocks
    private AccountRepository accountRepository;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        account1 = new Account(123L, "Owner 1", BigDecimal.TEN);
        account2 = new Account(456L, "Owner 2", BigDecimal.valueOf(20));
    }

    @Test
    void findAllShouldReturnAllAccounts() {
        when(jpaAccountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        List<Account> result = accountRepository.findAll();

        assertEquals(2, result.size());
        verify(jpaAccountRepository, times(1)).findAll();
    }

    @Test
    void findByNumberShouldReturnAccountIfExists() {
        when(jpaAccountRepository.findByNumber(123L)).thenReturn(Optional.of(account1));

        Optional<Account> result = accountRepository.findByNumber(123L);

        assertTrue(result.isPresent());
        assertEquals(123L, result.get().getNumber());
        verify(jpaAccountRepository, times(1)).findByNumber(123L);
    }

    @Test
    void saveShouldDelegateToJpa() {
        accountRepository.save(account1);

        verify(jpaAccountRepository, times(1)).save(account1);
    }

    @Test
    void findByNumberForUpdateShouldReturnAccountIfExists() {
        when(jpaAccountRepository.findByNumberForUpdate(123L)).thenReturn(Optional.of(account1));

        Optional<Account> result = accountRepository.findByNumberForUpdate(123L);

        assertTrue(result.isPresent());
        assertEquals(123L, result.get().getNumber());
        verify(jpaAccountRepository, times(1)).findByNumberForUpdate(123L);
    }
}
