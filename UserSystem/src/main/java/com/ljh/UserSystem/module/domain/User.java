package com.ljh.UserSystem.module.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 账号
     */
    @TableField(value = "user_account")
    private String user_account;

    /**
     * 用户头像
     */
    @TableField(value = "avatar_url")
    private String avatar_url;

    /**
     * 性别 0-女 1-男 2-保密
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 
     */
    @TableField(value = "profile")
    private String profile;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户状态，0为正常
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 用户角色 0-普通用户,1-管理员
     */
    @TableField(value = "role")
    private Integer role;

    /**
     * 
     */
    @TableField(value = "friend_ids")
    private String friend_ids;

    /**
     * 标签列表
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @TableLogic  //软删除
    private Integer is_delete;
}