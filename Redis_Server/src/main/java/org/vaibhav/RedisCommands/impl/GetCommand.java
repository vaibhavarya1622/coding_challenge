package org.vaibhav.RedisCommands.impl;

import org.vaibhav.MemoryStore.RedisStore;
import org.vaibhav.RedisCommands.Command;

public class GetCommand implements Command {
    private String[] args;
    private RedisStore redisStore = RedisStore.getInstance();

    public GetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        if(args.length>1) throw new RuntimeException("ERR wrong number of arguments for 'get' command");
        return this;
    }

    @Override
    public String execute() {
        Long ttl = redisStore.GETTTL(args[0]);
        if(ttl != null) {
            Long curr = System.currentTimeMillis();
            if(ttl<=curr) {
                redisStore.DEL(args[0]);
            }
        }
        return redisStore.GET(args[0]);
    }
}
