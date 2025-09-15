package br.com.fixture;

import br.com.domain.dto.TransactionDto;

import java.math.BigDecimal;

public class TransactionDtoFixture {

    public static TransactionDto transactionDto() {
        return new TransactionDto(123456L, new BigDecimal("100.00"));
    }

    public static TransactionDto transactionDto(Long accountNumber, BigDecimal amount) {
        return new TransactionDto(accountNumber, amount);
    }
}