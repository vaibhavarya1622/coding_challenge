package org.vaibhav.RedisCommands.impl;

import org.vaibhav.MemoryStore.RedisStore;
import org.vaibhav.RedisCommands.Command;

import java.util.Arrays;

public class SetCommand implements Command {
    private String[] args;
    private RedisStore redisStore = RedisStore.getInstance();

    public SetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        return this;
    }

    @Override
    public String execute() {
        boolean NX = Arrays.stream(args).anyMatch((arg)-> arg.equalsIgnoreCase("NX"));
        boolean XX = Arrays.stream(args).anyMatch((arg)->arg.equalsIgnoreCase("XX"));
        boolean GET = Arrays.stream(args).anyMatch((arg)->arg.equalsIgnoreCase("GET"));
        String response = null;
        if(NX) { //  Only set the key if it does not already exist.
            if(!redisStore.EXIST(args[0])) {
                redisStore.SET(args[0], args[1]);
                response = "OK";
            }
        }
        else if(XX) { // Only set the key if it already exists
            if(redisStore.EXIST(args[0])) {
                redisStore.SET(args[0], args[1]);
                response = "OK";
            }
        }
        else{
            redisStore.SET(args[0],args[1]);
            response = "OK";
        }
        if(GET) {
            response = redisStore.GET(args[0]);
        }
        long ttl = -1;
        for(int i=0;i< args.length;++i) {
            if(args[i].equalsIgnoreCase("EX")) {// Set the specified expire time, in seconds (a positive integer).
                ttl = System.currentTimeMillis()+Long.parseLong(args[i+1])*1000;
                break;
            }
            else if(args[i].equalsIgnoreCase("PX")) { // Set the specified expire time, in milliseconds (a positive integer)
                ttl = System.currentTimeMillis()+Long.parseLong(args[i+1]);
                break;
            }
        }
        if(ttl != -1) {
            redisStore.SETTTL(args[0],ttl);
        }
        return response;
    }
}
