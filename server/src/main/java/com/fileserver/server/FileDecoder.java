package com.fileserver.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FileDecoder extends ByteToMessageDecoder {

    private static final int fileLength = 4;
    private static final int fileNameLength = 128;

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    protected Object decode(@SuppressWarnings("UnusedParameters") ChannelHandlerContext ctx, ByteBuf in)
            throws Exception {
        if (in.readableBytes() < fileLength + fileNameLength) {
            return null;
        }
        in.markReaderIndex();
        int length = in.readInt();
        System.out.println(in.readableBytes() + ":" + length );
        if (in.readableBytes() <length + fileNameLength) {

            in.resetReaderIndex();

            return null;
        }

        return in.readRetainedSlice(length + fileNameLength);
    }
}
