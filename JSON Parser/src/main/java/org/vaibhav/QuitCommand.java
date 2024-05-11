package org.vaibhav;

import picocli.CommandLine.Command;

@Command(name = "quit", description = "this command is used to exit from application")
public class QuitCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Service is shutting down........");
        System.exit(0);
    }
}
