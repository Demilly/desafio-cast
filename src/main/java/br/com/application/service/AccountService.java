package br.com.application.service;

import br.com.application.exception.AccountNotFoundException;
import br.com.application.exception.BusinessException;
import br.com.application.validator.AccountValidators;
import br.com.application.validator.TransactionValidators;
import br.com.application.validator.TransferValidators;
import br.com.application.validator.Validator;
import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;
import br.com.domain.mapper.AccountMapper;
import br.com.domain.port.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountTransactionService transactionService;

    private final List<Validator<AccountCreateDto>> accountValidators = AccountValidators.getValidators();
    private final List<Validator<TransactionDto>> transactionValidators = TransactionValidators.getValidators();
    private final List<Validator<TransferDto>> transferValidators = TransferValidators.getValidators();

    public List<AccountDto> findAll() {
        return accountRepositoryPort.findAll()
                .stream()
                .map(accountMapper::toAccountDto)
                .collect(Collectors.toList());
    }

    public void createAccount(AccountCreateDto dto) {
        accountValidators.forEach(v -> v.validate(dto));

        Account acc = accountMapper.toAccount(dto);

        accountRepositoryPort.save(acc);
    }

    public void credit(TransactionDto dto) {
        transactionValidators.forEach(v -> v.validate(dto));

        transactionService.performWithOptimisticRetry(dto.getAccountNumber(), account -> {
            account.setBalance(account.getBalance().add(dto.getAmount()));
            return account;
        });
    }

    public void debit(TransactionDto dto) {
        transactionValidators.forEach(v -> v.validate(dto));

        transactionService.performWithOptimisticRetry(dto.getAccountNumber(), account -> {
            BigDecimal newBalance = account.getBalance().subtract(dto.getAmount());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("SALDO_INSUFICIENTE", "Saldo insuficiente para realizar débito");
            }
            account.setBalance(newBalance);
            return account;
        });
    }

    public void transfer(TransferDto dto, boolean pessimisticLock) {
        transferValidators.forEach(v -> v.validate(dto));

        if (pessimisticLock) {
            transactionService.transferWithPessimisticLock(dto);
        } else {
            transactionService.transferWithOptimisticRetry(dto);
        }
    }

    public AccountDto findAccount(Long number) {
        Account acc = accountRepositoryPort.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));

        return accountMapper.toAccountDto(acc);
    }
}
