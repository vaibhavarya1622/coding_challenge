package org.vaibhav.MultiThreadedIO;

import org.vaibhav.Utils.RequestParser;
import org.vaibhav.Utils.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IOWorker
        implements Runnable{
    private final Socket socket;

    public IOWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[1024];
            if(inputStream.available() == 0) {
                Thread.sleep(500);
            }
            while(inputStream.available()>0) {
                int len = inputStream.read(buffer);
                sb.append(new String(buffer,0,len));
            }
            if(sb.isEmpty()) {
                inputStream.close();
                outputStream.close();
                socket.close();
                return;
            }
//            System.out.println(sb);
            String path = RequestParser.parse(sb.toString());
            String response = ResponseParser.parse(path);
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            new RuntimeException("Something went wrong...",e);
        }

    }
}
