package org.vaibhav.MultiThreadedNIO;

import org.vaibhav.Utils.EncoderDecoder;
import org.vaibhav.Utils.RequestParser;
import org.vaibhav.Utils.ResponseParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIOWorker implements Runnable{
    private volatile SelectionKey selectionKey;
    public NIOWorker(SelectionKey key) {
        this.selectionKey = key;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        try {
            if(!socketChannel.isOpen()) {
                return;
            }
            int r = socketChannel.read(buffer);
            if(r<0) {
                socketChannel.close();
                return;
            }
            buffer.flip();
            String request = EncoderDecoder.bb_to_str(buffer);
            if(request.isEmpty()) {
                socketChannel.close();
                return;
            }
//            System.out.println(request);
            String path = RequestParser.parse(request);
            String response = ResponseParser.parse(path);
            socketChannel.write(EncoderDecoder.str_to_bb(response));
            socketChannel.close();
        } catch (IOException e) {
            try {
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error occured in closing the socketChannel",e);
            }
            throw new RuntimeException("Some error occured while reading from socket channel",e);
        }
    }
    private void close(SocketChannel socketChannel)
            throws IOException {
        socketChannel.close();
        selectionKey.cancel();
    }
}
