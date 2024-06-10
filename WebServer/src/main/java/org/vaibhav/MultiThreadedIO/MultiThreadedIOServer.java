package org.vaibhav.MultiThreadedIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadedIOServer implements Runnable {
    private final int port;
    private boolean isStopped;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public MultiThreadedIOServer(int port) {
        this.port = port;
    }
    private void openServerSocket() {
        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
        }
        catch (IOException ex) {
            throw new RuntimeException("Exception occured in starting the server on port: "+port);
        }

    }
    @Override
    public void run() {
        openServerSocket();
        try {
//            AtomicInteger counter = new AtomicInteger(0);
            while(!isStopped) {
                Socket socket = null;
                try{
                    socket = serverSocket.accept();
                }
                catch (IOException e) {
                    throw new RuntimeException("Error accepting connection");
                }
//                counter.getAndIncrement();
                this.executor.submit(new IOWorker(socket));
//                System.out.println(counter.get());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
