package org.vaibhav;

import org.vaibhav.IOServer.IOServer;
import org.vaibhav.NIOServer.NIOServer;

public class Main {
    public static void main(String[] args) {
        NIOServer server = new NIOServer(6380);
        server.start();
//        IOServer server = new IOServer(6380);
//        server.start();
    }
}
