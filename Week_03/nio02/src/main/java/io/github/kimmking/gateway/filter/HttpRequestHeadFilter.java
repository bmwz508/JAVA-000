package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author linmf
 * @Description
 * @date 2020/11/3 17:19
 */
public class HttpRequestHeadFilter implements HttpRequestFilter  {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx, HttpRequestFilterChain chain) {
        fullRequest.headers().add("nio", "linmingfa");
        chain.filter(fullRequest, ctx);
    }

}
