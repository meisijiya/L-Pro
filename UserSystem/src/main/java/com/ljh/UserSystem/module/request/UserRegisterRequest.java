package com.ljh.UserSystem.module.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 4481409407828041907L;

    private String userAccount;

    private String password;

    private String checkPassword;

    private String email;

    private String code;

}

