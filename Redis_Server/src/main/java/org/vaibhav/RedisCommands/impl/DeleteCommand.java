package org.vaibhav.RedisCommands.impl;

import org.vaibhav.MemoryStore.RedisStore;
import org.vaibhav.RedisCommands.Command;

import java.util.Arrays;

public class DeleteCommand implements Command {
    private final RedisStore redisStore = RedisStore.getInstance();
    private final String[] args;

    public DeleteCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        return this;
    }

    @Override
    public String execute() {
        String response = new ExistsCommand(args).execute();
        Arrays.stream(args).filter(redisStore::EXIST).forEach(redisStore::DEL);
        return response;
    }
}
