package org.vaibhav.RedisCommands;

public interface Command {
    Command validate();
    String execute();
}
