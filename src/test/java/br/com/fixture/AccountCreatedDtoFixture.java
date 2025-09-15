package br.com.fixture;

import br.com.domain.dto.AccountCreateDto;

import java.math.BigDecimal;

public class AccountCreatedDtoFixture {

    public static AccountCreateDto accountCreateDto() {
        return new AccountCreateDto(123456L, "Nome Completo do Proprietário", new BigDecimal("500.00"));
    }

    public static AccountCreateDto accountCreateDto(Long number, String ownerName, BigDecimal initialBalance) {
        return new AccountCreateDto(number, ownerName, initialBalance);
    }
}
