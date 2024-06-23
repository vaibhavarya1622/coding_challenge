package org.vaibhav.NIOServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServer {
    private int port;
    private boolean isStopped = false;
    private final List<SocketChannel> connectedClient = new ArrayList<>();
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public NIOServer(int port) {
        this.port = port;
    }

    public void register(Selector selector)
            throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        if(client.isOpen()) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            connectedClient.add(client);
        }
    }
    private void stop() {
        System.out.println("Server is shutting down.....");
        try{
            serverSocketChannel.close();
            executor.shutdown();
        }
        catch (IOException e) {
            throw new RuntimeException("Some error occured in shutting down the server.",e);
        }
        isStopped = true;
        System.out.println("Server shut down gracefully");
    }
    private void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Shutdown hook triggered");
            for(SocketChannel socket: connectedClient) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException("Some error occured in closing the client connection...",e);
                }
            }
            stop();
        }));
    }
    private void openServerSocket(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e) {
            throw new RuntimeException("Something went wrong in opening server socket at port+ "+port);
        }
    }
    public void start() {
        try{
            registerShutDownHook();
            openServerSocket(port);
            while(!isStopped) {
                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> itr = selectionKeySet.iterator();
                while(itr.hasNext()) {
                    SelectionKey key = itr.next();
                    if(key.isValid() && key.isAcceptable()) {
                        register(selector);
                    }
                    if(key.isValid() && key.isReadable()) {
                        synchronized (key) {
                            executor.submit(()->new NIOWorker(key).answer());
                        }
                    }
                    itr.remove();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}