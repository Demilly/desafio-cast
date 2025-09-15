package br.com.application.validator;

import br.com.application.exception.BusinessException;
import br.com.domain.dto.AccountCreateDto;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class AccountValidators {

    private static final int OWNER_NAME_MIN_LENGTH = 15;
    private static final int OWNER_NAME_MAX_LENGTH = 100;

    public static List<Validator<AccountCreateDto>> getValidators() {
        return asList(
                dto -> {
                    if (dto.getNumber() == null) {
                        throw new BusinessException("NUMERO_OBRIGATORIO", "Número da conta é obrigatório");
                    }
                },
                dto -> {
                    String ownerName = dto.getOwnerName() == null ? "" : dto.getOwnerName().trim();

                    if (ownerName.isEmpty()) {
                        throw new BusinessException("NOME_OBRIGATORIO", "Nome do proprietário é obrigatório");
                    }

                    if (ownerName.length() < OWNER_NAME_MIN_LENGTH) {
                        throw new BusinessException("NOME_CURTO", "Nome do proprietário deve ter no mínimo " + OWNER_NAME_MIN_LENGTH + " caracteres");
                    }

                    if (ownerName.length() > OWNER_NAME_MAX_LENGTH) {
                        throw new BusinessException("NOME_GRANDE", "Nome do proprietário não pode exceder " + OWNER_NAME_MAX_LENGTH + " caracteres");
                    }
                },
                dto -> Optional.ofNullable(dto.getBalance())
                        .filter(b -> b.signum() < 0)
                        .ifPresent(b -> {
                            throw new BusinessException("SALDO_NEGATIVO", "Saldo inicial não pode ser negativo");
                        })
        );
    }
}
