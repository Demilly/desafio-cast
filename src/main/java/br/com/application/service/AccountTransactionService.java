package br.com.application.service;

import br.com.application.exception.AccountNotFoundException;
import br.com.application.exception.BusinessException;
import br.com.application.exception.OptimisticLockRetryException;
import br.com.domain.Account;
import br.com.domain.dto.TransferDto;
import br.com.domain.port.AccountRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

@AllArgsConstructor
@Service
public class AccountTransactionService {

    private static final int MAX_RETRIES = 4;
    private final AccountRepositoryPort accountRepositoryPort;

    @Transactional
    public void performWithOptimisticRetry(Long accountNumber, UnaryOperator<Account> op) {
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                Account acc = accountRepositoryPort.findByNumber(accountNumber)
                        .orElseThrow(() -> new AccountNotFoundException(accountNumber));
                Account updated = op.apply(acc);
                accountRepositoryPort.save(updated);
                return;
            } catch (OptimisticLockingFailureException | javax.persistence.OptimisticLockException ex) {
                if (attempt >= MAX_RETRIES) {
                    throw new OptimisticLockRetryException("Falha por concorrência após " + attempt + " tentativas", ex);
                }
                sleepBackoff(attempt, 30);
            }
        }
    }

    @Transactional
    public void transferWithOptimisticRetry(TransferDto dto) {
        if (dto.getFromAccount().equals(dto.getToAccount())) {
            throw new BusinessException("CONTAS ORIGEM E DESTINO IGUAIS", "Conta origem e destino devem ser diferentes");
        }

        int attempt = 0;
        while (true) {
            attempt++;
            try {
                Account from = accountRepositoryPort.findByNumber(dto.getFromAccount())
                        .orElseThrow(() -> new AccountNotFoundException(dto.getFromAccount()));
                Account to = accountRepositoryPort.findByNumber(dto.getToAccount())
                        .orElseThrow(() -> new AccountNotFoundException(dto.getToAccount()));

                validatedAccountFromExistsBalance(dto, from);

                from.setBalance(from.getBalance().subtract(dto.getAmount()));
                to.setBalance(to.getBalance().add(dto.getAmount()));

                accountRepositoryPort.save(from);
                accountRepositoryPort.save(to);
                return;
            } catch (OptimisticLockingFailureException | javax.persistence.OptimisticLockException ex) {
                if (attempt >= MAX_RETRIES) {
                    throw new OptimisticLockRetryException("Falha por concorrência após " + attempt + " tentativas", ex);
                }
                sleepBackoff(attempt, 50);
            }
        }
    }

    private static void validatedAccountFromExistsBalance(TransferDto dto, Account from) {
        if (from.getBalance().subtract(dto.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("SALDO INSUFICIENTE", "Saldo insuficiente na conta origem");
        }
    }

    @Transactional
    public void transferWithPessimisticLock(TransferDto dto) {
        if (dto.getFromAccount().equals(dto.getToAccount())) {
            throw new BusinessException("CONTAS ORIGEM E DESTINO IGUAIS", "Conta origem e destino devem ser diferentes");
        }

        Long first = dto.getFromAccount().compareTo(dto.getToAccount()) <= 0 ? dto.getFromAccount() : dto.getToAccount();
        Long second = first.equals(dto.getFromAccount()) ? dto.getToAccount() : dto.getFromAccount();

        Account a1 = accountRepositoryPort.findByNumberForUpdate(first)
                .orElseThrow(() -> new AccountNotFoundException(first));
        Account a2 = accountRepositoryPort.findByNumberForUpdate(second)
                .orElseThrow(() -> new AccountNotFoundException(second));

        Account from = dto.getFromAccount().equals(a1.getNumber()) ? a1 : a2;
        Account to = dto.getToAccount().equals(a1.getNumber()) ? a1 : a2;

        if (from.getBalance().subtract(dto.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("SALDO INSUFICIENTE", "Saldo insuficiente na conta origem");
        }

        from.setBalance(from.getBalance().subtract(dto.getAmount()));
        to.setBalance(to.getBalance().add(dto.getAmount()));

        accountRepositoryPort.save(from);
        accountRepositoryPort.save(to);
    }

    private void sleepBackoff(int attempt, long baseMillis) {
        try {
            TimeUnit.MILLISECONDS.sleep(baseMillis * attempt * 10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new OptimisticLockRetryException("Thread interrompida durante backoff", ex);
        }
    }
}
