package org.vaibhav;

import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("> ");
            String input = reader.readLine();
            if(input.startsWith("ccwc")) {
                new CommandLine(new wcCommand()).execute(input.split(" "));
            }
            else if(input.startsWith("quit")) {
                new CommandLine(new QuitCommand()).execute();
            }
        }
    }
}