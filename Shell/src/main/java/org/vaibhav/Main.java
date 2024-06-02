package org.vaibhav;

import org.vaibhav.Commands.CDCommand;
import org.vaibhav.Commands.CatCommand;
import org.vaibhav.Commands.HistoryCommand;
import org.vaibhav.Commands.LSCommand;
import org.vaibhav.Commands.PWDCommand;
import org.vaibhav.Util.Output;
import org.vaibhav.Util.WorkingDir;
import picocli.CommandLine;
import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main {
    private static void writeToHistory(String command)
            throws IOException {
        command += "\n";
        Files.write(Path.of(".ccsh_history"),command.getBytes(),StandardOpenOption.APPEND);
    }
    private static void registerSignal() {
        Signal.handle(new Signal("INT"),sig -> {// The signal may not work instantly. It will work when the interupption will be entertained.
            System.out.println("Interrupted by CTRL+C. Exiting...");
            System.exit(0);
        });
    }
    public static void main(String[] args)
            throws IOException {
        registerSignal();
        while(true) {
            WorkingDir dir = WorkingDir.getInstance();
            Output output = Output.getInstance();
            System.out.print(dir.getCurrentDir().toString() + " ccsh> ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String read = br.readLine();
            writeToHistory(read);
            String[] commands = read.split("\\|");
            for(String input: commands) {
                input = input.trim();
                if(input.equals("ls")) {
                    new CommandLine(new LSCommand()).execute();
                }
                else if(input.equals("pwd")) {
                    new CommandLine(new PWDCommand()).execute();
                }
                else if(input.startsWith("cd")) {
                    String[] arg = input.split(" ");
                    new CommandLine(new CDCommand()).execute(arg[1]);
                }
                else if(input.startsWith("cat")) {
                    String[] argument = input.split(" ");
                    new CommandLine(new CatCommand()).execute(Arrays.copyOfRange(argument,1,argument.length));
                }
                else if(input.equals("history")) {
                    new CommandLine(new HistoryCommand()).execute();
                }
                else if(input.equals("exit")) {
                    System.exit(0);
                }
                else {
                    System.out.println("No such command found!");
                }
            }
            output.printOutput();
        }
    }
}
