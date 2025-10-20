package com.ljh.UserSystem.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    private static final String DEFAULT_SUCCESS_MESSAGE = "ok";
    private static final String EMPTY_DESCRIPTION = "";

    /**
     * 创建一个表示操作成功的响应对象
     *
     * @param data 业务数据
     * @param <T> 业务数据的类型
     * @return 包含成功状态和业务数据的响应对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 创建成功的响应对象xx
     * @param data 响应数据
     * @param message 响应消息
     * @param <T> 数据类型泛型
     * @return 成功的BaseResponse对象，状态码为0
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(0, data, message, EMPTY_DESCRIPTION);
    }

    /**
     * 创建成功的响应对象
     * @param data 响应数据
     * @param message 响应消息
     * @param description 响应描述
     * @param <T> 数据类型泛型
     * @return 创建成功的BaseResponse对象
     */
    public static <T> BaseResponse<T> success(T data, String message, String description) {
        return new BaseResponse<>(0, data, message, description);
    }

    /**
     * 创建一个表示操作失败的响应对象
     *
     * @param errorCode 错误码枚举对象，包含错误码和错误信息
     * @param <T> 返回数据的类型
     * @return 包含失败状态的响应对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode cannot be null");
        }
        return new BaseResponse<>(errorCode);
    }

    /**
     * 创建一个表示操作失败的响应对象
     *
     * @param code 错误码
     * @param message 错误信息
     * @param description 错误描述
     * @param <T> 返回数据的类型
     * @return 包含失败状态的响应对象
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 创建一个表示操作失败的响应对象
     *
     * @param errorCode 错误码枚举对象
     * @param message 错误信息
     * @param description 错误描述
     * @param <T> 返回数据的类型
     * @return 包含失败状态的响应对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode cannot be null");
        }
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 创建一个表示操作失败的响应对象
     *
     * @param errorCode 错误码枚举对象
     * @param description 错误描述
     * @param <T> 返回数据的类型
     * @return 包含失败状态的响应对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode cannot be null");
        }
       return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);

    }

    /**
     * 创建错误的响应对象
     * @param code 错误状态码
     * @param message 错误消息
     * @param <T> 返回数据的类型
     * @return 错误的BaseResponse对象，数据部分为null
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message, EMPTY_DESCRIPTION);
    }
}
