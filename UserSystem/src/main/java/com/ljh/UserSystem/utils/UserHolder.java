package com.ljh.UserSystem.utils;

import com.ljh.UserSystem.module.dto.UserDTO;

/**
 * UserHolder 类用于在当前线程中存储和获取用户信息。
 * 每个 HTTP 请求由独立的线程处理
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        System.out.println("线程"+Thread.currentThread().getName()+"保存用户信息");
        tl.set(user);
    }

    public static UserDTO getUser(){
        System.out.println("线程"+Thread.currentThread().getName()+"获取用户信息");
        return tl.get();
    }

    public static void removeUser(){
        System.out.println("线程"+Thread.currentThread().getName()+"移除用户信息");
        tl.remove();
    }
}
