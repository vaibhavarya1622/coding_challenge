package org.vaibhav.MultiThreadedNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadedNIOServer implements Runnable{
    private final int port;
    private Selector selector;
    private boolean isStopped = false;
    private ServerSocketChannel serverSocketChannel;
    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    public MultiThreadedNIOServer(int port) {
        this.port = port;
    }
    private void register()
            throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        if(client.isOpen()) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
    }
    private void stop()
            throws IOException {
        this.isStopped = true;
        this.executor.shutdown();
        this.serverSocketChannel.close();
        this.selector.close();
    }
    @Override
    public void run() {
        try{
//            AtomicInteger count = new AtomicInteger(0);
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(!isStopped) {
                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> itr = selectionKeySet.iterator();
                while(itr.hasNext()) {
                    SelectionKey key = itr.next();
                    if(key.isAcceptable()) {
                        register();
                    }
                    if(key.isValid() && key.isReadable()) {
                        synchronized (key) { // Had to synchronize otherwise multiple threads were submitted
                            // Might be possible data is coming in multiple chunks
//                            count.getAndIncrement();
                            Future<SelectionKey> f = this.executor.submit(new NIOWorker(key),key);
                            f.get().cancel();
                        }
                    }
                    itr.remove();
                }
//                System.out.println(count.get());
            }
            System.out.println("Server shutting down....");
            stop();
        }
        catch (Exception e){
            try {
                selector.close();
                if(serverSocketChannel != null)
                    serverSocketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error occured in selector",e);
            }

        }
    }
}
