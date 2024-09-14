package com.app.Bank.dto;

import com.app.Bank.common.Constants;
import com.app.Bank.common.Constants.Role;
import com.app.Bank.common.Constants.Branch;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;
@Data
public class UserDTO {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Field must contain only alphabetic characters.")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password must be alphanumeric")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN card number format.")
    @NotBlank(message = "Pan is mandatory")
    private String pan;

    @Pattern(regexp = "\\d{12}", message = "Aadhaar card number must be exactly 12 digits.")
    @NotBlank(message = "Aadhaar is mandatory")
    private String aadhar;

    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 digits.")
    @NotBlank(message = "Mobile is mandatory")
    private String mobile;

    private String address;

    @Pattern(regexp = "Male|Female|Other", message = "Gender must be either Male, Female, or Other.")
    @NotBlank(message = "Gender is mandatory")
    private String gender;
    @NotNull(message = "DOB is mandatory")
    @Past(message = "DOB must be in past")
    private Date dob;
    @NotNull(message = "Role is mandatory")
//    @Pattern(regexp = "ADMIN|BANK_MANAGER|CUSTOMER", message = "Role must be either ADMIN, BANK_MANAGER, or CUSTOMER.")
    private Role role;
    @NotNull(message = "Branch is mandatory")
//    @Pattern(regexp = "DEL|MUM|BLR", message = "Branch must be either DEL,MUM, or BLR.")
    private Branch branch;
}
