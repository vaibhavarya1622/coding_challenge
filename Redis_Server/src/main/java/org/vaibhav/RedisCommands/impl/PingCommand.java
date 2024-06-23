package org.vaibhav.RedisCommands.impl;

import org.vaibhav.RedisCommands.Command;

public class PingCommand implements Command {
    private String[] args;
    public PingCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        if(args.length>1) throw new RuntimeException("ERR wrong number of arguments for 'ping' command");
        return this;
    }

    @Override
    public String execute() {
        return args.length == 0 ? "PONG":args[0];
    }
}
