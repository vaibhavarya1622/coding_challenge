package org.vaibhav.Utils.EncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

public class Decoder {
    public Charset charset = StandardCharsets.UTF_8;
    public CharsetDecoder decoder = charset.newDecoder();

    public String bb_to_str(ByteBuffer buffer) {
        CharBuffer out = CharBuffer.allocate(1024);
        decoder.reset();
        while(buffer.hasRemaining()) {
            CoderResult result = decoder.decode(buffer,out,false);
            if(result.isError()) {
                System.out.println("Something went wrong in decoder");
                break;
            }
            if(result.isOverflow()) {
                CharBuffer newOut = CharBuffer.allocate(out.capacity()*2);
                out.flip();
                newOut.put(out);
                out = newOut;
                decoder.decode(buffer,out,false);
            }
        }
        decoder.decode(buffer,out,true);
        decoder.flush(out);
        out.flip();
        return out.toString();
    }
}
