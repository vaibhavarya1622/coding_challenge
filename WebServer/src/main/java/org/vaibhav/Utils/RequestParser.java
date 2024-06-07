package org.vaibhav.Utils;

public class RequestParser {
    public static String parse(String request) {
        String[] reqArray = request.split("[\r\n]");
        String path = reqArray[0].split(" ")[1];
        return path;
    }
}
