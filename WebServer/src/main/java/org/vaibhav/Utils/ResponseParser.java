package org.vaibhav.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResponseParser {
    public static String parse(String urlPath)
            throws IOException {
        if(urlPath.endsWith(".ico")) { //This hack is for chrome. Chrome triggers this request
            return "HTTP/1.1 200 OK \r \n";
        }
        if(urlPath.equals("/") || urlPath.equals("/index.html")) {
            Path path = Path.of("src/main/resources/www","index.html");
            String data = Files.readString(path);
            String response = "HTTP/1.1 200 OK \r\n";
            response += "Content-Type: text/html\r\n";
            response += "Content-Length: " + data.getBytes(StandardCharsets.UTF_8).length + "\r\n\r\n";
            response += data;
            return response;
        }
        else{
            return "HTTP/1.1 400 NOT FOUND \r \n";
        }
    }
}
