package com.ljh.UserSystem.exception;

import com.ljh.UserSystem.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    private final int code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 构造一个业务异常实例
     * @param message 异常消息
     * @param code 异常码
     * @param description 异常描述
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * 根据错误码构造业务异常实例
     * @param errorCode 错误码枚举对象
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 根据错误码和自定义消息构造业务异常实例
     * @param errorCode 错误码枚举对象
     * @param  message 自定义异常消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    /**
     * 根据错误码和自定义描述构造业务异常实例
     * @param errorCode 错误码枚举对象
     * @param message 自定义异常消息
     * @param description 自定义异常描述
     */
    public BusinessException(ErrorCode errorCode, String message,String description) {
        super(message);
        this.code = errorCode.getCode();
        this.description = description;
    }

    /**
     * 获取异常码
     * @return 异常码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取异常描述
     * @return 异常描述
     */
    public String getDescription() {
        return description;
    }

}
