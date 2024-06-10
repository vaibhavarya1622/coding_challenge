package org.vaibhav;

import org.vaibhav.MultiThreadedIO.MultiThreadedIOServer;
import org.vaibhav.MultiThreadedNIO.MultiThreadedNIOServer;

public class Main {
    public static void main(String[] args) {
//        NIOServer nioServer = new NIOServer(8080);
//        new Thread(nioServer,"nioSingleThreadedServer").start();
//        IOServer ioServer = new IOServer(8080);
//        new Thread(ioServer, "SingleThreadedIOServer").start();
        new Thread(new MultiThreadedNIOServer(8080)).start();
//        new Thread(new MultiThreadedIOServer(8080)).start();
    }
}