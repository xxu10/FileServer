package com.fileserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyFileServer {
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));


    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void init() throws Exception{


        Thread thread  = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline p = ch.pipeline();

                                    p.addLast(new FileDecoder());

                                    p.addLast(new NettyServerHandler());
                                }
                            });


                    ChannelFuture f = b.bind(PORT).sync();
                    System.out.println("----Server Started----");


                    f.channel().closeFuture().sync();
                }catch(Exception e) {
                    System.out.println("----Server error----" + e.getLocalizedMessage());
                }
                finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        });

        thread.start();

    }
}
