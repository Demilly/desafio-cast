package br.com.application.validator;

import br.com.application.exception.BusinessException;
import br.com.domain.dto.TransferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferValidatorsTest {

    private List<Validator<TransferDto>> validators;

    @BeforeEach
    void setUp() {
        validators = TransferValidators.getValidators();
    }

    @Test
    void shouldThrowIfFromAccountIsInvalid() {
        TransferDto dto = new TransferDto(null, 12345L, BigDecimal.TEN);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(0).validate(dto)
        );
        assertEquals("NUMERO_CONTA_OBRIGATORIO", ex.getCode());
    }

    @Test
    void shouldThrowIfToAccountIsInvalid() {
        TransferDto dto = new TransferDto(12345L, null, BigDecimal.TEN);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("NUMERO_CONTA_OBRIGATORIO", ex.getCode());
    }

    @Test
    void shouldThrowIfAmountIsZero() {
        TransferDto dto = new TransferDto(12345L, 67890L, BigDecimal.ZERO);

        BusinessException exFrom = assertThrows(BusinessException.class, () ->
                validators.get(0).validate(dto)
        );
        assertEquals("VALOR_INVALIDO", exFrom.getCode());

        BusinessException exTo = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("VALOR_INVALIDO", exTo.getCode());
    }

    @Test
    void shouldPassForValidTransfer() {
        TransferDto dto = new TransferDto(12345L, 67890L, BigDecimal.valueOf(100));
        validators.forEach(v -> v.validate(dto));
    }
}
