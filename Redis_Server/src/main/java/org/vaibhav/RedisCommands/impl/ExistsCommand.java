package org.vaibhav.RedisCommands.impl;

import org.vaibhav.MemoryStore.RedisStore;
import org.vaibhav.RedisCommands.Command;

import java.util.Arrays;

public class ExistsCommand implements Command {
    private RedisStore redisStore = RedisStore.getInstance();
    private String[] args;

    public ExistsCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Command validate() {
        return this;
    }

    @Override
    public String execute() {
        return String.valueOf(Arrays.stream(args).filter(redisStore::EXIST).mapToInt((arg)->1).sum());
    }
}
