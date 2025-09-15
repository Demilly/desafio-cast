package br.com.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateDto {
    @NotNull
    private Long number;

    @NotBlank
    private String ownerName;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;
}
