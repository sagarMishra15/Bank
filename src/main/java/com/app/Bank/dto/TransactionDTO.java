package com.app.Bank.dto;

import com.app.Bank.common.Constants;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransactionDTO {
    @Pattern(regexp = "\\d{10,16}", message = "Account number must be between 10 and 16 digits.")
    @NotBlank(message = "Account Number is mandatory")
    private String beneAccNo;
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format.")
    @NotBlank(message = "IFSC Code is mandatory")
    private String beneIfsc;
    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private double amount;
    private String beneName;
    private String remarks;
    @Min(value = 1000, message = "TPIN must be exactly 4 digits.")
    @Max(value = 9999, message = "TPIN must be exactly 4 digits.")
    private int tpin;
//    @Pattern(regexp = "IMPS|NEFT|RTGS|UPI", message = "Mode of Payment must be either IMPS, NEFT, RTGS, or UPI.")
    private Constants.Mop mop;
//    private String latlong; //check
//    private Constants.TxnStatus status;
}
