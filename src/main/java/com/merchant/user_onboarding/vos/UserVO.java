package com.merchant.user_onboarding.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private String userId;

    private String userName;

    private Long age;

    private String department;

    private String dateOfBirth;

    private String registeredDate;

    private String updatedDate;
}
