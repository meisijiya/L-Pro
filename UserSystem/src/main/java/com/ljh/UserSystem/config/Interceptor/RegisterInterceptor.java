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

import static com.ljh.UserSystem.constant.EmailConstant.EMAIL_CODE;
import static com.ljh.UserSystem.constant.UserConstant.USER_LOGIN_STATE;

public class RegisterInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //打印请求url
        System.out.println(request.getRequestURI());
        //1.获取session
        HttpSession session = request.getSession();
        //2.获取session中的用户的emailCode
        Object user = session.getAttribute(EMAIL_CODE);
        //3.判断用户是否是否持有emailCode
        if(user == null) {
            //4.不存在就拦截，返回错误码
            throw new BusinessException(ErrorCode.EMAIL_ERROR,"请先发送验证码");
        }
        //6.放行
        return true;
    }

}