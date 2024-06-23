package org.vaibhav.RedisCommands.impl;

import org.vaibhav.MemoryStore.RedisStore;
import org.vaibhav.RedisCommands.Command;

public class IncrCommand implements Command {
    private final RedisStore redisStore = RedisStore.getInstance();
    private final String[] args;

    public IncrCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        if(args.length>1) throw new RuntimeException("ERR wrong number of arguments for 'incr' command");
        try{
            if(redisStore.EXIST(args[0]))
                Integer.parseInt(redisStore.GET(args[0]));
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("ERR value is not an integer or out of range");
        }
        return this;
    }

    @Override
    public String execute() {
        redisStore.SET(args[0], redisStore.GET(args[0])==null?
                "0": String.valueOf(Integer.parseInt(redisStore.GET(args[0]))+1));
        return redisStore.GET(args[0]);
    }
}
