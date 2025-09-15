package br.com.application.service;

import br.com.application.exception.AccountNotFoundException;
import br.com.application.exception.BusinessException;
import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;
import br.com.domain.mapper.AccountMapper;
import br.com.domain.port.AccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @Mock
    private AccountTransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllShouldReturnAccounts() {
        Account acc1 = new Account(1L, "Alice", BigDecimal.TEN);
        Account acc2 = new Account(2L, "Bob", BigDecimal.valueOf(20));

        AccountDto dto1 = new AccountDto(1L, "Alice", BigDecimal.TEN);
        AccountDto dto2 = new AccountDto(2L, "Bob", BigDecimal.valueOf(20));

        when(accountRepositoryPort.findAll()).thenReturn(Arrays.asList(acc1, acc2));
        when(accountMapper.toAccountDto(acc1)).thenReturn(dto1);
        when(accountMapper.toAccountDto(acc2)).thenReturn(dto2);

        List<AccountDto> result = accountService.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getOwnerName());
        assertEquals(BigDecimal.valueOf(20), result.get(1).getBalance());
    }

    @Test
    void createAccountShouldSaveAccount() {
        AccountCreateDto dto = new AccountCreateDto(789L, "Charlie Brow Junior", BigDecimal.valueOf(100));
        Account account = new Account(789L, "Charlie Brow Junior", BigDecimal.valueOf(100));

        when(accountMapper.toAccount(dto)).thenReturn(account);

        accountService.createAccount(dto);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepositoryPort, times(1)).save(captor.capture());

        Account saved = captor.getValue();
        assertEquals(Long.valueOf(789L), saved.getNumber());
        assertEquals("Charlie Brow Junior", saved.getOwnerName());
        assertEquals(BigDecimal.valueOf(100), saved.getBalance());
    }

    @Test
    void creditShouldCallTransactionService() {
        TransactionDto dto = new TransactionDto(123L, BigDecimal.valueOf(50));

        accountService.credit(dto);

        verify(transactionService, times(1))
                .performWithOptimisticRetry(eq(123L), any());
    }

    @Test
    void debitShouldCallTransactionServiceIfSufficientBalance() {
        TransactionDto dto = new TransactionDto(123L, BigDecimal.valueOf(5));

        accountService.debit(dto);

        verify(transactionService, times(1))
                .performWithOptimisticRetry(eq(123L), any());
    }

    @Test
    void debitShouldThrowIfInsufficientBalance() {
        TransactionDto dto = new TransactionDto(123L, BigDecimal.valueOf(20));

        doAnswer(invocation -> {
            UnaryOperator<Account> op = invocation.getArgument(1);
            Account acc = new Account(123L, "Alice", BigDecimal.TEN);
            op.apply(acc);
            return null;
        }).when(transactionService).performWithOptimisticRetry(eq(123L), any());

        BusinessException ex = assertThrows(BusinessException.class, () -> accountService.debit(dto));
        assertEquals("SALDO_INSUFICIENTE", ex.getCode());
    }

    @Test
    void transferShouldCallPessimisticLock() {
        TransferDto dto = new TransferDto(123L, 456L, BigDecimal.valueOf(10));

        accountService.transfer(dto, true);

        verify(transactionService, times(1)).transferWithPessimisticLock(dto);
        verify(transactionService, never()).transferWithOptimisticRetry(any());
    }

    @Test
    void transferShouldCallOptimisticRetry() {
        TransferDto dto = new TransferDto(123L, 456L, BigDecimal.valueOf(10));

        accountService.transfer(dto, false);

        verify(transactionService, times(1)).transferWithOptimisticRetry(dto);
        verify(transactionService, never()).transferWithPessimisticLock(any());
    }

    @Test
    void findAccountShouldReturnAccountDtoIfExists() {
        Account acc = new Account(123L, "Alice", BigDecimal.TEN);
        AccountDto dto = new AccountDto(123L, "Alice", BigDecimal.TEN);

        when(accountRepositoryPort.findByNumber(123L)).thenReturn(Optional.of(acc));
        when(accountMapper.toAccountDto(acc)).thenReturn(dto);

        AccountDto result = accountService.findAccount(123L);

        assertEquals(dto, result);
    }

    @Test
    void findAccountShouldThrowIfNotFound() {
        when(accountRepositoryPort.findByNumber(999L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findAccount(999L));
    }
}
