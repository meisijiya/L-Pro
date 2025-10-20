package com.ljh.UserSystem.exception.handler;

import com.ljh.UserSystem.common.BaseResponse;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.common.ResultUtils;
import com.ljh.UserSystem.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 业务异常处理器
     * 捕获 BusinessException 异常并返回统一的错误响应
     *
     * @param e 业务异常对象，包含错误码、错误信息和错误描述
     * @return BaseResponse<?> 统一的错误响应结果，包含错误码、错误信息和错误描述
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        // 记录业务异常日志，包括异常信息和堆栈跟踪
        log.error("businessException: " + e.getMessage(), e);
        // 返回业务异常对应的错误响应
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 运行时异常处理器
     * 捕获 RuntimeException 异常并返回系统错误响应
     *
     * @param e 运行时异常对象
     * @return BaseResponse<?> 统一的系统错误响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        // 记录运行时异常日志
        log.error("runtimeException", e);
        // 返回系统错误响应，使用默认的系统错误码
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
