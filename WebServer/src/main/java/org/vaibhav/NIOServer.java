package org.vaibhav;

import org.vaibhav.Utils.EncoderDecoder;
import org.vaibhav.Utils.RequestParser;
import org.vaibhav.Utils.ResponseParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServer implements Runnable {
    private int port;
    public NIOServer(int port) {
        this.port = port;
    }

    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        if(client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
    }

    private void answer(ByteBuffer byteBuffer, SelectionKey selectionKey)
            throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        int r = channel.read(byteBuffer);
        if(r == -1) {
            channel.close();
            selectionKey.cancel();
            return;
        }
        byteBuffer.flip();
        String request = EncoderDecoder.bb_to_str(byteBuffer);
        byteBuffer.clear();
        String path = RequestParser.parse(request);
        String response = ResponseParser.parse(path);
        channel.write(EncoderDecoder.str_to_bb(response));
        channel.close();
        selectionKey.cancel();
    }

    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }
                    if (key.isValid() && key.isReadable()) {
                        answer(buffer, key);
                    }
                    iter.remove();
                }
            }
        }catch (IOException e) {}
    }
}
