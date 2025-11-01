package com.merchant.user_onboarding.vos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    @NotNull
    @Size(min=2, max=50, message = "Should be more than 3 and less than 50 character!")
    private String userId;

    @NotBlank(message = "Name cannot be blank!")
    @Size(min=3, max=20, message = "Should not be more than 20 Characters!")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*(?: [a-zA-Z0-9]+)*$", message = "Username must start with a letter and contain only letters and numbers.")
    @Pattern(regexp = "^([A-Z][a-z]+)(\\s[A-Z][a-z]+){0,2}\\s([A-Z][a-z]+)$", message = "Only first letter of the name should be in capital")
    private String userName;

    @Min(value = 10, message = "Age should be more than 10")
    @Max(value = 150, message = "Age should not be more than 150")
    private Long age;

    @Pattern(regexp = "(?i)ENGINEERING|MANAGEMENT|SALES", message = "Category must be ENGINEERING, MANAGEMENT or SALES")
    private String department;

    @NotNull(message = "DOB cannot be null")
    private String dateOfBirth;

    private String registeredDate;

    private String updatedDate;
}
