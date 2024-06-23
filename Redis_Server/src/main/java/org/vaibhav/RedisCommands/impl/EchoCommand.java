package org.vaibhav.RedisCommands.impl;

import org.vaibhav.RedisCommands.Command;

public class EchoCommand implements Command {
    private final String[] args;

    public EchoCommand(String[] args) {
        this.args = args;
    }
    @Override
    public Command validate() {
        if(args.length>1) throw new RuntimeException("ERR wrong number of arguments for 'echo' command");
        return this;
    }
    @Override
    public String execute() {
        return args[0];
    }
}
