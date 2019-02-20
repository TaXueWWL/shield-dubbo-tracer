package com.snowalker.shield.dubbo.tracer;

import java.util.UUID;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/20 14:06
 * @className UuidUtil
 * @desc UUID工具类
 */
class UuidUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
