package com.github.jannikemmerich.javacrypticclient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.json.simple.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClient {

    private static WebSocketClient instance;

    private static final boolean EPOLL = Epoll.isAvailable();
    private Channel channel;

    public String status = "waiting";

    public Map<String, JSONObject> unhandledPackets = new HashMap<>();

    public WebSocketClient(String url) throws Exception {
        instance = this;

        URI uri = URI.create(url);

        String scheme = uri.getScheme() == null ? "wss" : uri.getScheme();
        String host = uri.getHost() == null ? "ws.cryptic-game.net" : uri.getHost();
        int port;
        if(uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported");
            System.exit(-1);
        }

        boolean ssl = "wss".equalsIgnoreCase(scheme);

        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        WebSocketHandler handler = new WebSocketHandler(WebSocketClientHandshakerFactory
                .newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()));
            channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            if (ssl) {
                                socketChannel.pipeline().addLast(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                                        .build().newHandler(socketChannel.alloc(), host, port));
                            }
                            socketChannel.pipeline().addLast(new HttpClientCodec(), new HttpObjectAggregator(65536), handler);
                        }
                    }).connect(host, port).sync().channel();
            handler.handshakeFuture().sync();
    }

    public static WebSocketClient getInstance() {
        return instance;
    }

    public Channel getChannel() {
        return channel;
    }
}
