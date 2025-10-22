package com.ljh.UserSystem.controller;
import com.ljh.UserSystem.common.BaseResponse;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.common.ResultUtils;
import com.ljh.UserSystem.module.dto.UserDTO;
import com.ljh.UserSystem.module.request.UserLoginRequest;
import com.ljh.UserSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserController#userLogin 方法的单元测试
 */
class UserControllerTest {

    @InjectMocks
    private UserController userController; // 被测试的 Controller 实例

    @Mock
    private UserService userService; // 模拟 UserService

    @Mock
    private HttpServletRequest request; // 模拟 HttpServletRequest

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
    }

    /**
     * 测试用例 TC001: userLoginRequest 为 null
     */
    @Test
    void testUserLogin_NullRequest_ReturnsParamsError() {
        BaseResponse<UserDTO> response = userController.userLogin(null, request);
        assertNotNull(response);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), response.getCode());
    }

    /**
     * 测试用例 TC002: userAccount 为 null
     */
    @Test
    void testUserLogin_NullUserAccount_ReturnsParamsError() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount(null);
        loginRequest.setPassword("123");

        BaseResponse<UserDTO> response = userController.userLogin(loginRequest, request);
        assertNotNull(response);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), response.getCode());
    }

    /**
     * 测试用例 TC003: userAccount 为空字符串
     */
    @Test
    void testUserLogin_EmptyUserAccount_ReturnsParamsError() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount("");
        loginRequest.setPassword("123");

        BaseResponse<UserDTO> response = userController.userLogin(loginRequest, request);
        assertNotNull(response);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), response.getCode());
    }

    /**
     * 测试用例 TC004: userPassword 为 null
     */
    @Test
    void testUserLogin_NullPassword_ReturnsParamsError() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount("admin");
        loginRequest.setPassword(null);

        BaseResponse<UserDTO> response = userController.userLogin(loginRequest, request);
        assertNotNull(response);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), response.getCode());
    }

    /**
     * 测试用例 TC005: userPassword 为空字符串
     */
    @Test
    void testUserLogin_EmptyPassword_ReturnsParamsError() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount("admin");
        loginRequest.setPassword("");

        BaseResponse<UserDTO> response = userController.userLogin(loginRequest, request);
        assertNotNull(response);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), response.getCode());
    }

    /**
     * 测试用例 TC006: 正常输入，模拟 userService.userLogin 返回成功
     */
    @Test
    void testUserLogin_ValidInput_ReturnsSuccess() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUserAccount("admin");
        loginRequest.setPassword("123");

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setId(1L);
        mockUserDTO.setUser_account("admin");

        when(userService.userLogin(anyString(), anyString(), any(HttpServletRequest.class)))
                .thenReturn(mockUserDTO);

        BaseResponse<UserDTO> response = userController.userLogin(loginRequest, request);
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals(mockUserDTO, response.getData());
    }
}
