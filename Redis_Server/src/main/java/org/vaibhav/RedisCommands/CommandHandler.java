package org.vaibhav.RedisCommands;

import org.vaibhav.RedisCommands.impl.DecrCommand;
import org.vaibhav.RedisCommands.impl.DeleteCommand;
import org.vaibhav.RedisCommands.impl.EchoCommand;
import org.vaibhav.RedisCommands.impl.ExistsCommand;
import org.vaibhav.RedisCommands.impl.GetCommand;
import org.vaibhav.RedisCommands.impl.IncrCommand;
import org.vaibhav.RedisCommands.impl.PingCommand;
import org.vaibhav.RedisCommands.impl.SetCommand;
import org.vaibhav.Utils.SerializerDeserializer.Deserializer;
import org.vaibhav.Utils.SerializerDeserializer.Serializer;
import java.util.Arrays;

public class CommandHandler {
    public String handle(String request) {
        Serializer serializer = new Serializer();
        Deserializer deserializer = new Deserializer();
        try {
            String[] args = deserializer.parseRequest(request);
            return switch (args[0].toUpperCase()) {
                case "PING" -> serializer.parseSimpleString(new PingCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "ECHO" -> serializer.parseSimpleString(new EchoCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "SET" -> {
                    String value = new SetCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute();
                    yield (value == null) ? serializer.parseNull():serializer.parseSimpleString(value);
                }
                case "GET" -> {
                    String value = new GetCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute();
                    yield (value == null) ? serializer.parseNull() : serializer.parseSimpleString(value);
                }
                case "EXISTS" -> serializer.parseSimpleString(new ExistsCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "DEL" -> serializer.parseSimpleString(new DeleteCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "INCR" -> serializer.parseSimpleString(new IncrCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "DECR" -> serializer.parseSimpleString(new DecrCommand(Arrays.copyOfRange(args,1,args.length)).validate().execute());
                case "EXIT" -> throw new RuntimeException("Client requested disconnect");
                default -> serializer.parseError("Invalid command");
            };
        }
        catch (RuntimeException e) {
            return serializer.parseError(e.getMessage());
        }
    }
}
