package org.vaibhav;

import org.vaibhav.Utils.RequestParser;
import org.vaibhav.Utils.ResponseParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IOServer implements Runnable{
    private boolean isStopped = false;
    private ServerSocket serverSocket;
    private int port;
    private Thread runningThread = null;

    IOServer(int port){
        this.port = port;
    }

    public void stop() {
        this.isStopped = true;
        try{
            serverSocket.close();
        }catch (IOException e){
            throw new RuntimeException("Error closing server");
        }
    }
    public void run() {
        synchronized (this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket(port);
        while (!isStopped) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                    throw new RuntimeException("Error accepting connections");
            }
            try {
                processClientRequest(clientSocket);
            } catch (Exception e) {
                try {
                    closeConnection(clientSocket);
                } catch (IOException ex) {
                    throw new RuntimeException("Error closing client connection",ex);
                }
                throw new RuntimeException("Error processing client request",e);
            }
        }
    }
    private void processClientRequest(Socket socket)
            throws IOException, InterruptedException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        if(inputStream.available() == 0) {
            Thread.sleep(1000);
        }
        while(inputStream.available()>0) {
           int len = inputStream.read(buffer,0,1024);
           stringBuilder.append(new String(buffer,0,len));
        }
//        System.out.print(stringBuilder);
        if(stringBuilder.isEmpty()) {
            inputStream.close();
            outputStream.close();
            throw new RuntimeException("Request is empty");
        }
        String path = RequestParser.parse(stringBuilder.toString());
        String response = ResponseParser.parse(path);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        closeConnection(socket);
    }
    private void closeConnection(Socket socket)
            throws IOException {
        socket.close();
    }
    private void openServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Error opening port at: "+port);
        }
    }
}
