package br.com.domain.mapper;

import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toAccount(AccountCreateDto dto) {
        if (dto == null) return null;

        return new Account(
                dto.getNumber(),
                dto.getOwnerName(),
                dto.getBalance()
        );
    }

    public AccountDto toAccountDto(Account account) {
        if (account == null) return null;

        AccountDto dto = new AccountDto();
        dto.setNumber(account.getNumber());
        dto.setOwnerName(account.getOwnerName());
        dto.setBalance(account.getBalance());
        return dto;
    }

}