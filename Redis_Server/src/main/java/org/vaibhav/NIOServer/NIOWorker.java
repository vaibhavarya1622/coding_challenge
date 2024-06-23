package org.vaibhav.NIOServer;

import org.vaibhav.RedisCommands.CommandHandler;
import org.vaibhav.Utils.EncoderDecoder.Decoder;
import org.vaibhav.Utils.EncoderDecoder.Encoder;
import org.vaibhav.Utils.SerializerDeserializer.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIOWorker {
    private final CommandHandler commandHandler = new CommandHandler();
    private final Encoder encoder = new Encoder();
    private final Decoder decoder = new Decoder();
    private final SelectionKey selectionKey;
    private final Serializer serializer = new Serializer();

    public NIOWorker(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void answer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SocketChannel channel =(SocketChannel) selectionKey.channel();
        int r=0;
        try {
            r = channel.read(byteBuffer);
        } catch (IOException e) {
            try {
                channel.close();
            } catch (IOException ex) {
                throw new RuntimeException("Something went wrong while closing the read/write channel",e);
            }
            throw new RuntimeException("Something went wrong while reading....",e);
        }
        if(r == -1) {
            try {
                channel.close();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong while closing the read/write channel",e);
            }
            selectionKey.cancel();
            return;
        }
        byteBuffer.flip();
        String request = decoder.bb_to_str(byteBuffer);
        try {
            if(request.isEmpty()) {
                return;
            }
            if(channel.isOpen()) {
                String response = commandHandler.handle(request);
                ByteBuffer buffer = encoder.str_to_bb(response);
                channel.write(buffer);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Something went wrong while writing....",e);
        }
    }
}
