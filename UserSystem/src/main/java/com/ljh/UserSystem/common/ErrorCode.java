package com.ljh.UserSystem.common;

/**
 * 错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", "请检查请求参数是否正确"),
    NULL_ERROR(40001, "请求数据为空", "请求数据不能为空"),
    OPERATION_ERROR(40002, "操作失败", "操作失败" ),
    NOT_LOGIN(40100, "未登录", "请先登录"),
    EMAIL_ERROR(40200, "邮箱操作异常","" ),
    NO_AUTH(40101, "无权限", "您没有执行该操作的权限"),
    FORBIDDEN(40301, "禁止操作", "该操作被禁止"),
    SYSTEM_ERROR(50000, "系统内部异常", "服务器内部出现错误");



    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
