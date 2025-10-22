package com.ljh.UserSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljh.UserSystem.common.BaseResponse;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.common.ResultUtils;

import com.ljh.UserSystem.exception.BusinessException;
import com.ljh.UserSystem.module.domain.User;
import com.ljh.UserSystem.module.dto.UserDTO;
import com.ljh.UserSystem.module.request.UserLoginRequest;
import com.ljh.UserSystem.module.request.UserRegisterRequest;
import com.ljh.UserSystem.service.UserService;
import com.ljh.UserSystem.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.ljh.UserSystem.constant.UserConstant.ADMIN_ROLE;
import static com.ljh.UserSystem.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //判断传入的值是否为空
        if(userLoginRequest == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogin(userAccount,userPassword,request));
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userLoginRequest) {
        //判断传入的值是否为空
        if(userLoginRequest == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getPassword();
        String checkPassword = userLoginRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        //返回用户id
        return ResultUtils.success(userService.userRegister(userAccount,userPassword,checkPassword), "用户注册成功");
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if(request == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result, "用户登出成功");
    }

    @GetMapping("/current")
    public BaseResponse<UserDTO> getCurrentUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getCurrentUser(request), "获取当前用户成功");
    }

    @GetMapping("/userList")
    public BaseResponse<Page<UserDTO>> getUserList(
            @RequestParam(required = false) String userAccount,
            @RequestParam(defaultValue = "1")long current,
            @RequestParam(defaultValue = "10") long size,
            HttpServletRequest request) {
        if (userAccount==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,ErrorCode.PARAMS_ERROR.getMessage(),"请输入用户名");
        }
        //判断是否为管理员
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,ErrorCode.NO_AUTH.getMessage(),"管理员才能操作");
        }
        return ResultUtils.success(userService.getUserList(userAccount,current,size), "获取用户列表成功");
    }

    @GetMapping("/delete")
    public BaseResponse<Integer> userDelete(@RequestParam(required = false)String userAccount, HttpServletRequest request) {
        //如果没有传入删除参数，则默认注销自己
        if(userAccount== null){
            return ResultUtils.success(userService.userDelete(userAccount,request), "注销成功");
        }else {
            if(!isAdmin(request)){
                throw new BusinessException(ErrorCode.NO_AUTH,ErrorCode.NO_AUTH.getMessage(),"管理员才能删除其他用户");
            }else {
                return ResultUtils.success(userService.userDelete(userAccount,request), "删除成功");
            }
        }

    }

    @PostMapping("/infoUpdate")
    public BaseResponse<Integer> userInfoUpdate(@RequestBody UserDTO user, HttpServletRequest request) {
        return ResultUtils.success(userService.userInfoUpdate(user,request), "用户信息更新成功");
    }

    /**
     * 是否为管理员
     *
     * @param request HttpServletRequest
     * @return  boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        UserDTO user = UserHolder.getUser();
        return user != null && user.getRole() == ADMIN_ROLE;
    }
}
