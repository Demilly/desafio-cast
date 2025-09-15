package br.com.domain.mapper;

import br.com.domain.Account;
import br.com.domain.dto.AccountCreateDto;
import br.com.domain.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Account toAccount(AccountCreateDto accountCreateDto);

    AccountDto toAccountDto(Account account);
}
