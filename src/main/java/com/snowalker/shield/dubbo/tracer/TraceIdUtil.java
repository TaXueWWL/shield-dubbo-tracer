package com.snowalker.shield.dubbo.tracer;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/20 14:12
 * @className TraceIdUtil
 * @desc TraceId工具类，也可看做TraceId的上下文。基于ThreadLocal在线程内隐式传递TraceId
 */
public class TraceIdUtil {

    /**使用InheritableThreadLocal便于在主子线程间传递参数*/
    private static final ThreadLocal<String> TRACE_ID = new InheritableThreadLocal<>();

    public TraceIdUtil() {
    }

    /**
     * 从当前线程局部变量获取TraceId
     * 首次调用该方法会生成traceId，后续每次都从线程上下文获取
     * @return
     */
    public static String getTraceId() {
        return TRACE_ID.get();
    }

    public static void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static void removeTraceId() {
        TRACE_ID.remove();
    }

}
