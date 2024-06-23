package org.vaibhav.Utils.EncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class Encoder {
    public Charset charset = StandardCharsets.UTF_8;
    public CharsetEncoder encoder = charset.newEncoder();

    public ByteBuffer str_to_bb(String msg) {
        encoder.reset();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.wrap(msg);
        while(charBuffer.hasRemaining()) {
            CoderResult result = encoder.encode(charBuffer,buffer,false);
            if(result.isError()) {
                System.out.println("Something went wrong in encoder");
                break;
            }
            if(result.isOverflow()) {
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
}
