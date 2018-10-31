package com.fileserver.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ClientApplication {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(new StringDecoder());
                            p.addLast(new NettyClientHandler());

                        }
                    });


            ChannelFuture f = b.connect(HOST, PORT).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();

        } finally {
            group.shutdownGracefully();
        }

    }
}
