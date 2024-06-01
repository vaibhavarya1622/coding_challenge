package org.vaibhav;

import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args)
            throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("> ");
            String input = br.readLine();
            if(input.startsWith("cccat")) {
                String[] argument = input.split(" ");
                new CommandLine(new CatCommand()).execute(Arrays.copyOfRange(argument,1,argument.length));
            }
        }
    }
}
