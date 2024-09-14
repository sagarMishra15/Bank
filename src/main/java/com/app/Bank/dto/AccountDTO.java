package com.app.Bank.dto;

import com.app.Bank.common.Constants;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;
//    @Positive(message = "Balance must be positive")
//    @NotBlank(message = "Balance is mandatory")
//    private double balance;
//    @Positive(message = "Hold Balance must be positive")
//    private double holdBalance;
    @Pattern(regexp = "\\d{10,16}", message = "Account number must be between 10 and 16 digits.")
    @NotBlank(message = "Account Number is mandatory")
    private String accNo;
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format.")
    @NotBlank(message = "IFSC Code is mandatory")
    private String ifsc;
    @Min(value = 1000, message = "TPIN must be exactly 4 digits.")
    @Max(value = 9999, message = "TPIN must be exactly 4 digits.")
    private int tpin;
    @NotNull(message = "Branch is mandatory")
    private Constants.Branch branch;
    private Constants.Status status;
}
