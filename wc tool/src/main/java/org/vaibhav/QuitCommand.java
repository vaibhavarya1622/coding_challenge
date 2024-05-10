package org.vaibhav;

import picocli.CommandLine.Command;

@Command(name = "quit", description = "this is a quit command")
public class QuitCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Shutting down......");
        System.exit(0);
    }
}
