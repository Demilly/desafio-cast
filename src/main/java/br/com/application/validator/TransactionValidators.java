package br.com.application.validator;

import br.com.application.exception.BusinessException;
import br.com.domain.dto.TransactionDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TransactionValidators {

    public static List<Validator<TransactionDto>> getValidators() {
        return Arrays.asList(
                dto -> {
                    if (dto.getAccountNumber() == null) {
                        throw new BusinessException("NUMERO_CONTA_OBRIGATORIO", "Número da conta é obrigatório");
                    }
                },
                dto -> Optional.ofNullable(dto.getAmount())
                        .filter(a -> a.signum() <= 0)
                        .ifPresent(a -> {
                            throw new BusinessException("VALOR_INVALIDO", "O valor da transação deve ser maior que zero");
                        })
        );
    }
}
