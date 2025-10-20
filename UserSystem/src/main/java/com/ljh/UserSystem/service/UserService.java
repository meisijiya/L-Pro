package com.ljh.UserSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.UserSystem.common.BaseResponse;
import com.ljh.UserSystem.module.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljh.UserSystem.module.request.DeleteRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 22923
* @description 针对表【user】的数据库操作Service
* @createDate 2025-10-18 15:35:25
*/
public interface UserService extends IService<User> {


    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    Long userRegister(String userAccount, String userPassword, String checkPassword);

    boolean userLogout(HttpServletRequest request);

    User getCurrentUser(HttpServletRequest request);

    Page<User> getUserList(String userAccount,long current, long size);

    int delete(String userAccount, HttpServletRequest request);


    int update(User user, HttpServletRequest request);
}
