package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.inbound.HttpFilterHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.ListIterator;

public class DefaultHttpRequestFilterChain implements HttpRequestFilterChain {

    private final List<HttpRequestFilter> filters;
    private final ChannelInboundHandler handler;
    private final HttpRequestFilter currentFilter;
    private final DefaultHttpRequestFilterChain chain;

    public DefaultHttpRequestFilterChain(List<HttpRequestFilter> filters, ChannelInboundHandler handler) {
        this.filters = filters;
        this.handler = handler;
        DefaultHttpRequestFilterChain chain = initChain(filters, handler);
        this.currentFilter = chain.currentFilter;
        this.chain = chain.chain;
    }

    private DefaultHttpRequestFilterChain(List<HttpRequestFilter> filters, ChannelInboundHandler handler, HttpRequestFilter currentFilter, DefaultHttpRequestFilterChain chain) {
        this.filters = filters;
        this.handler = handler;
        this.currentFilter = currentFilter;
        this.chain = chain;
    }

     private static DefaultHttpRequestFilterChain initChain(List<HttpRequestFilter> filters, ChannelInboundHandler handler){
         DefaultHttpRequestFilterChain chain = new DefaultHttpRequestFilterChain(filters, handler, null, null);
         for (ListIterator<HttpRequestFilter> iterator = filters.listIterator(filters.size()); iterator.hasPrevious(); chain = new DefaultHttpRequestFilterChain(filters, handler, iterator.previous(), chain)) {
         }
         return chain;
     }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        if (this.currentFilter != null && this.chain != null) {
            this.currentFilter.filter(fullRequest, ctx, this.chain);
        } else {
            try {
                this.handler.channelRead(ctx, fullRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
