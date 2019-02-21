package com.snowalker.shield.dubbo.tracer;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/20 13:53
 * @className TraceIdGenerator
 * @desc TraceId生成器
 */
public class TraceIdGenerator {

    /**
     * 消费端创建TraceId,并设置到线程上下文中
     * 该方法只调用一次
     * @return
     */
    public static String createTraceId() {
        // 创建的同时就设置到上下文中
        String traceId = getTraceid();
        TraceIdUtil.setTraceId(traceId);
        return traceId;
    }

    /**
     * 生成32位traceId
     * @return
     */
    private static String getTraceid() {
        String result = "";
        String ip = "";

        // 获取本地ipv4地址
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (Exception var5) {
            return result;
        }

        // 根据.截取为String数组
        String[] ipAddressInArray = ip.split("\\.");
        // 拼装为字符串,将每一个元素转换为16进制
        for(int i = 3; i >= 0; --i) {
            Integer id = Integer.parseInt(ipAddressInArray[3 - i]);
            result = result + String.format("%02x", id);
        }
        // 拼装时间戳及随机数
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        result = result + simpleDateFormat.format(new Date()) + UuidUtil.getUuid().substring(0, 7);
        return result;
    }

}
