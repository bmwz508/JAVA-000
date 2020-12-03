package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.DefaultHttpRequestFilterChain;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestHeadFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.FutureTask;

public class HttpFilterHandler extends ChannelInboundHandlerAdapter {

    private DefaultHttpRequestFilterChain chain;

    public HttpFilterHandler(List<HttpRequestFilter> filters, ChannelInboundHandler handler) {
        this.chain = new DefaultHttpRequestFilterChain(filters, handler);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object request){
        this.chain.filter((FullHttpRequest) request, ctx);
    }

}
