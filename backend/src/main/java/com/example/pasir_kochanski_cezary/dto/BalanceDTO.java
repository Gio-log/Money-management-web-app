package com.example.pasir_kochanski_cezary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor

public class BalanceDTO {
    @NotBlank
    private double totalIncome;

    @NotBlank
    private double totalExpense;

    @NotBlank
    private double balance;

}
