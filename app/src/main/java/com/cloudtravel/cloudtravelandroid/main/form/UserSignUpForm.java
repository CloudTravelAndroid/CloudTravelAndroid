package com.cloudtravel.cloudtravelandroid.main.form;

import lombok.Data;

@Data
public class UserSignUpForm {

    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private Integer universityID;
}
