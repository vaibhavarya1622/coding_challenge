package org.vaibhav.IOServer;

import org.vaibhav.RedisCommands.CommandHandler;
import org.vaibhav.Utils.SerializerDeserializer.Deserializer;
import org.vaibhav.Utils.SerializerDeserializer.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IOWorker {
    private final Socket clientSocket;

    public IOWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void handle() {
        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            char[] buffer = new char[1024];
            int n;
            CommandHandler commandHandler = new CommandHandler();
            while ((n = in.read(buffer)) > 0) {
                String request = new String(buffer, 0, n);
                out.write(commandHandler.handle(request));
                out.flush();
            }
        }
        catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error occured in input/outstream with client",e);
        }
    }
}
