package org.vaibhav.Utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class EncoderDecoder {
    public static Charset charset = StandardCharsets.UTF_8;
    public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();

    public static ByteBuffer str_to_bb(String msg) {
        encoder.reset();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.wrap(msg);
        while(charBuffer.hasRemaining()) {
            CoderResult coderResult = encoder.encode(charBuffer, buffer,false);
            if(coderResult.isError()) {
                System.out.println("Something went wrong in str_to_bb");
                break;
            }
            if(coderResult.isOverflow()) {
                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()*2);
                buffer.flip();
                newBuffer.put(buffer);
                buffer = newBuffer;
                encoder.encode(charBuffer,buffer,false);
            }
        }
        encoder.encode(charBuffer,buffer,true);
        encoder.flush(buffer);
        buffer.flip();
        return buffer;
    }

    public static String bb_to_str(ByteBuffer buffer) {
        CharBuffer outBuffer = CharBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();
        decoder.reset();
        while(buffer.hasRemaining()) {
            CoderResult coderResult = decoder.decode(buffer,outBuffer,false);
            if(coderResult.isOverflow()) {
                outBuffer.flip();
                sb.append(outBuffer);
                outBuffer.clear();
            }
            if(coderResult.isError()) {
                System.out.println("There is some error in bb_to_str");
                break;
            }
        }
        decoder.decode(buffer,outBuffer,true);
        CoderResult coderResult = decoder.flush(outBuffer);
        if(coderResult.isOverflow()) {
            outBuffer.flip();
            sb.append(outBuffer);
            outBuffer.clear();
        }
        outBuffer.flip();
        sb.append(outBuffer);
        return sb.toString();
    }
}
