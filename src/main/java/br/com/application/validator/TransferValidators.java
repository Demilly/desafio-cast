package br.com.application.validator;

import br.com.domain.dto.TransactionDto;
import br.com.domain.dto.TransferDto;

import java.util.List;

import static java.util.List.of;

public class TransferValidators {

    public static List<Validator<TransferDto>> getValidators() {
        return of(
                dto -> {
                    TransactionValidators.getValidators()
                            .forEach(v -> v.validate(
                                    new TransactionDto(dto.getFromAccount(), dto.getAmount())
                            ));
                },
                dto -> {
                    TransactionValidators.getValidators()
                            .forEach(v -> v.validate(
                                    new TransactionDto(dto.getToAccount(), dto.getAmount())
                            ));
                }
        );
    }
}
