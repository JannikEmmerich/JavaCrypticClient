package com.github.jannikemmerich.javacrypticclient.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.UUID;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("WebSocket disconnected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        if(!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(channel, (FullHttpResponse) msg);
                System.out.println("WebSocket connected");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        TextWebSocketFrame frame = (TextWebSocketFrame) msg;

        JSONObject json = (JSONObject) new JSONParser().parse(frame.text());

        if(!json.containsKey("tag")) {
            System.out.println(json.toJSONString());
            return;
        }

        UUID tag = UUID.fromString((String) json.get("tag"));

        if(Request.requests.containsKey(tag)) {
            Request.requests.remove(tag);
            Request.requests.put(tag, (JSONObject) json.get("data"));
        } else {
            System.out.println(json.toJSONString());
        }
    }
}
