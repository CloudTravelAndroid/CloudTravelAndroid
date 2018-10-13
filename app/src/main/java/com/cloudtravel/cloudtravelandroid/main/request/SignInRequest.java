package com.cloudtravel.cloudtravelandroid.main.request;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignInRequest extends CloudTravelBaseRequest{
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
