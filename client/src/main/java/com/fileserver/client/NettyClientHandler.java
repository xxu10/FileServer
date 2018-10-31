package com.fileserver.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final byte[] request;

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    public NettyClientHandler() {
        request = "hello server,im a client".getBytes();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- client already disconnected----");

        ctx.fireChannelInactive();
    }

    public void sendFile(ChannelHandlerContext ctx ,String fileName,boolean closeConnect) {
        FileInputStream fileInputStream = null;
        int count = 0;

        try {

            File file = new File("/Users/xuxin/Desktop/test.jpg");
            fileInputStream = new FileInputStream(file);

            byte[] buf = new byte[1024];
            ByteBuf message = null;

            System.out.println("send file size:" + fileInputStream.available() + "," + file.length());
            message = Unpooled.buffer(4);
            message.writeInt((int) file.length());
            ctx.write(message);

            if(fileName.length()<128) {
                int appendNum = 128 - fileName.length();
                for(int i =0;i<appendNum;++i) {
                    fileName += " ";
                }
            }
            System.out.println("send file size:" + fileName.length() + "," + file.length());
            message = Unpooled.buffer(128);
            message.writeBytes(fileName.getBytes("utf-8"));
            ctx.write(message);

            int len = -1;
            while (-1 != (len = fileInputStream.read(buf))) {
                System.out.println("--- client send file ----" + len);

                count += len;
                message = Unpooled.wrappedBuffer(buf, 0, len);
                ctx.writeAndFlush(message);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("--- client send file over----" + count);
        if(closeConnect) {
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        System.out.println("--- client already connected----");

        sendFile(ctx, "hello1.jpg",false);
        sendFile(ctx, "hello22.jpg",true);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println(atomicInteger.getAndIncrement() + "receive from server:" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
