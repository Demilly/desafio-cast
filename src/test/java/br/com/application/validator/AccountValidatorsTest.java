package br.com.application.validator;

import br.com.application.exception.BusinessException;
import br.com.domain.dto.AccountCreateDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountValidatorsTest {

    private final List<Validator<AccountCreateDto>> validators = AccountValidators.getValidators();

    @Test
    void shouldThrowIfAccountNumberIsNull() {
        AccountCreateDto dto = new AccountCreateDto(null, "Nome Proprietário Teste", BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(0).validate(dto)
        );
        assertEquals("NUMERO_OBRIGATORIO", ex.getCode());
    }

    @Test
    void shouldThrowIfOwnerNameIsNull() {
        AccountCreateDto dto = new AccountCreateDto(12345L, null, BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("NOME_OBRIGATORIO", ex.getCode());
    }

    @Test
    void shouldThrowIfOwnerNameIsTooShort() {
        AccountCreateDto dto = new AccountCreateDto(12345L, "Nome Curto", BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("NOME_CURTO", ex.getCode());
    }

    @Test
    void shouldThrowIfOwnerNameIsTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("N");
        }
        String longName = sb.toString();

        AccountCreateDto dto = new AccountCreateDto(12345L, longName, BigDecimal.ZERO);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(1).validate(dto)
        );
        assertEquals("NOME_GRANDE", ex.getCode());
    }

    @Test
    void shouldThrowIfInitialBalanceIsNegative() {
        AccountCreateDto dto = new AccountCreateDto(12345L, "Nome Proprietário Teste", BigDecimal.valueOf(-10));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                validators.get(2).validate(dto)
        );
        assertEquals("SALDO_NEGATIVO", ex.getCode());
    }

    @Test
    void shouldPassAllValidationsForValidDto() {
        AccountCreateDto dto = new AccountCreateDto(12345L, "Nome Proprietário Teste", BigDecimal.valueOf(100));

        assertDoesNotThrow(() -> validators.forEach(v -> v.validate(dto)));
    }
}
