package com.ljh.UserSystem.module.dto;

import lombok.Data;


/**
 * 用户信息传输对象
 */
@Data
public class UserDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String user_account;

    /**
     * 用户头像
     */
    private String avatar_url;

    /**
     * 性别 0-女 1-男 2-保密
     */
    private Integer gender;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态，0为正常
     */
    private Integer status;

    /**
     * 用户角色 0-普通用户,1-管理员
     */
    private Integer role;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 朋友列表
     */
    private String friend_ids;

}
