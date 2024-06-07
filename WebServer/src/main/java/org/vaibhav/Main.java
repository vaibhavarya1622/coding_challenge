package org.vaibhav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        if(client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
    }

    private static void answer(ByteBuffer byteBuffer, SelectionKey selectionKey)
            throws IOException, InterruptedException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        int r = channel.read(byteBuffer);
        if(r == -1) {
            return;
        }
        byteBuffer.flip();
        String request = EncoderDecoder.bb_to_str(byteBuffer);
        System.out.println(request);
        byteBuffer.clear();
        String path = RequestParser.parse(request);
        String response = ResponseParser.parse(path);
        channel.write(EncoderDecoder.str_to_bb(response));
        channel.close();
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(8080));
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
            }
            iter.remove();
        }
    }
}