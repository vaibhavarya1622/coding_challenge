package org.vaibhav.IOServer;

import org.vaibhav.Utils.SerializerDeserializer.Deserializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOServer {
    private int port;
    private final Deserializer serializeCommands = new Deserializer();
    private ServerSocket serverSocket;
    private boolean isStopped = false;
    private final List<Socket> connectedClient = new ArrayList<>();
    private final ExecutorService executors = Executors.newFixedThreadPool(10);

    public IOServer(int port) {
        this.port = port;
    }

    private void closeConnection(Socket socket) {
        System.out.println("Closing the socket with client....");
        try{
            socket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error occured in closing the socket with client",e);
        }
    }
    private void stop() {
        System.out.println("Server is shutting down.....");
        try{
            serverSocket.close();
            executors.shutdown();
        }
        catch (IOException e) {
            throw new RuntimeException("Some error occured in shutting down the server.",e);
        }
        isStopped = true;
        System.out.println("Server shut down gracefully");
    }
    private void registerShutDownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Shutdown hook triggered");
            for(Socket socket: connectedClient) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException("Some error occured in closing the client connection...",e);
                }
            }
            stop();
        }));
    }
    public void start() {
        registerShutDownHook();
        openServerSocket(port);
        while(!isStopped) {
            Socket clientSocket = null;
            try{
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong in accepting connections");
            }
            try{
                connectedClient.add(clientSocket);
                Socket finalClientSocket = clientSocket;
                executors.submit(()->new IOWorker(finalClientSocket).handle());
            }
            catch (Exception e) {
                closeConnection(clientSocket);
                throw new RuntimeException("Error occured in IO Worker",e);
            }
        }
    }

    private void openServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            throw new RuntimeException("Something went wrong in opening server socket at port+ "+port);
        }
    }
}
