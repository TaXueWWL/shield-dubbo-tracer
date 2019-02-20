package com.snowalker.shield.dubbo.tracer;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/20 14:35
 * @className TraceIdFilter
 * @desc TraceId Dubbo过滤器，用于在服务之间传递TraceId
 * 根据服务端/消费端选择具体的TraceId传递策略
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class TraceIdFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        String traceId = "";
        if (rpcContext.isConsumerSide()) {
            if (StringUtils.isBlank(TraceIdUtil.getTraceId())) {
                // 根调用，生成TraceId
                traceId = TraceIdGenerator.createTraceId();
                LOGGER.debug("[TraceIdFilter-consumerSide] TraceId in TraceIdUtil is null,create new TraceId=" + traceId);
            } else {
                // 后续调用，从Rpc上下文取出并设置到线程上下文
                traceId = TraceIdUtil.getTraceId();
                LOGGER.debug("[TraceIdFilter-consumerSide] TraceId in TraceIdUtil exists,TraceId=" + traceId);
            }
            TraceIdUtil.setTraceId(traceId);
            RpcContext.getContext().setAttachment(TraceIdConst.TRACE_ID, TraceIdUtil.getTraceId());
            LOGGER.debug("[TraceIdFilter-consumerSide] set TraceId to TraceIdUtil and RpcContext,TraceId={}", traceId);
        }
        if (rpcContext.isProviderSide()) {
            // 服务提供方，从Rpc上下文获取traceId
            traceId = RpcContext.getContext().getAttachment(TraceIdConst.TRACE_ID);
            TraceIdUtil.setTraceId(traceId);
            LOGGER.debug("[TraceIdFilter-providerSide] get TraceId from RpcContext and set it to thr trace Context,TraceId={}", traceId);
        }
        Result result = invoker.invoke(invocation);
        return result;
    }
}
