package br.com.fixture;

import br.com.domain.dto.TransferDto;

import java.math.BigDecimal;

public class TransferDtoFixture {

    public static TransferDto transferDto() {
        return new TransferDto(123456L, 654321L, new BigDecimal("100.00"));
    }

    public static TransferDto transferDto(Long fromAccount, Long toAccount, BigDecimal amount) {
        return new TransferDto(fromAccount, toAccount, amount);
    }
}
