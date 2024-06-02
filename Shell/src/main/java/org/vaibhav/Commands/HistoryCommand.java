package org.vaibhav.Commands;

import org.vaibhav.Util.Output;
import picocli.CommandLine.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Command(name = "history", description = "this will write the history of all commands written")
public class HistoryCommand implements Runnable{
    @Override
    public void run() {
        Output output = Output.getInstance();
        StringBuilder sb = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(1);
        try(Stream<String> stream = Files.lines(Path.of(".ccsh_history"))) {
            stream.forEach((s)->sb.append(counter.getAndIncrement()).append(" ").append(s).append('\n'));
            output.setOutput(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
