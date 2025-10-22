package com.ljh.UserSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.UserSystem.common.BaseResponse;
import com.ljh.UserSystem.module.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljh.UserSystem.module.dto.UserDTO;
import com.ljh.UserSystem.module.request.DeleteRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 22923
* @description 针对表【user】的数据库操作Service
* @createDate 2025-10-18 15:35:25
*/
public interface UserService extends IService<User> {


    UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    Long userRegister(String userAccount, String userPassword, String checkPassword);

    boolean userLogout(HttpServletRequest request);

    UserDTO getCurrentUser(HttpServletRequest request);

    Page<UserDTO> getUserList(String userAccount,long current, long size);

    int userDelete(String userAccount, HttpServletRequest request);


    int userInfoUpdate(UserDTO user, HttpServletRequest request);
}
