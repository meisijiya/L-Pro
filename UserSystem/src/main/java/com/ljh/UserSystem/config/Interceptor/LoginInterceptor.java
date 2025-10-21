package com.ljh.UserSystem.config.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.exception.BusinessException;
import com.ljh.UserSystem.module.dto.UserDTO;
import com.ljh.UserSystem.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.ljh.UserSystem.constant.UserConstant.USER_LOGIN_STATE;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser(); // 清理 ThreadLocal 数据
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //打印请求url
        System.out.println(request.getRequestURI());
        //1.获取session
        HttpSession session = request.getSession();
        //2.获取session中的用户
        Object user = session.getAttribute(USER_LOGIN_STATE);
        //3.判断用户是否已登录
        if(user == null) {
            //4.不存在就拦截，返回错误码
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //5.登录就保存用户信息到ThreadLocal  默认情况：BeanUtil.copyProperties 方法默认按照 字段名完全匹配 的原则进行复制
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        UserHolder.saveUser(userDTO);
        //打印
        System.out.println("线程"+UserHolder.getUser().getId());
        //6.放行
        return true;
    }
}