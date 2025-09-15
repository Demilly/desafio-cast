package br.com.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotNull
    private Long number;

    @NotBlank
    private String ownerName;

    @NotNull
    private BigDecimal balance;
}
