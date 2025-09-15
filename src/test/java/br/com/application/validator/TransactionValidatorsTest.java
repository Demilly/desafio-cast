package br.com.application.validator;

import br.com.application.exception.BusinessException;
import br.com.domain.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionValidatorsTest {

    private List<Validator<TransactionDto>> validators;

    @BeforeEach
    void setUp() {
        validators = TransactionValidators.getValidators();
    }

    @Test
    void shouldThrowIfAccountNumberIsNull() {
        TransactionDto dto = new TransactionDto(null, BigDecimal.TEN);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(0).validate(dto)
        );
        assertEquals("NUMERO_CONTA_OBRIGATORIO", ex.getCode());
    }

    @Test
    void shouldThrowIfAmountIsZero() {
        TransactionDto dto = new TransactionDto(12345L, BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("VALOR_INVALIDO", ex.getCode());
    }

    @Test
    void shouldThrowIfAmountIsNegative() {
        TransactionDto dto = new TransactionDto(12345L, BigDecimal.valueOf(-10));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("VALOR_INVALIDO", ex.getCode());
    }

    @Test
    void shouldPassForValidTransaction() {
        TransactionDto dto = new TransactionDto(12345L, BigDecimal.valueOf(100));
        validators.forEach(v -> v.validate(dto));
    }
}
