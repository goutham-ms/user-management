package com.merchant.user_onboarding.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_SER_NUMBER")
    private Integer id;

    @Column(name ="USR_ID")
    private String userId;

    @Column(name ="USR_NAME")
    private String userName;

    @Column(name ="USR_AGE")
    private Long age;

    @Column(name ="DEPT")
    private String department;

    @Column(name ="USR_DOB")
    private LocalDate dateOfBirth;

    @Column(name ="USR_REG_DATE")
    private LocalDateTime registeredDate;

    @Column(name = "USR_LAST_UPDATED")
    private LocalDateTime lastUpdated;

}
