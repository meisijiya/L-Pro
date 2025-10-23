package com.ljh.UserSystem.utils;


import cn.hutool.core.util.IdUtil;

import java.util.UUID;

public class CodeGeneratorUtil {
    /**
     * 生成指定长度的验证码
     * @param length 长度
     * @return
     */
    public static String generateCode(int length){
        return UUID.randomUUID().toString().substring(0, length);
    }

    /**
     * 雪花算法生成用户注册的id
     */
    public static long snowflake(){
        return IdUtil.getSnowflakeNextId();
    }
}
