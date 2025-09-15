package br.com.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    @NotNull
    private Long fromAccount;

    @NotNull
    private Long toAccount;

    @NotNull
    @Positive
    private BigDecimal amount;
}