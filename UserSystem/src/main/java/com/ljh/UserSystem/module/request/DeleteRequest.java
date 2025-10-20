package com.ljh.UserSystem.module.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID= 6230581521159116024L;

    private String userAccount;

}
