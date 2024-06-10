package org.vaibhav.Utils;

public class RequestParser {
    public static String parse(String request) {
        String[] reqArray = request.split("[\r\n]");
        return reqArray[0].split(" ")[1];
    }
}
