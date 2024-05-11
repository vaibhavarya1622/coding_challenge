package org.vaibhav;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Command(name = "parse",description = "this command is used to parse the json from file")
public class ParseCommand implements Runnable {
    @Parameters(index = "0", description = "The command name")
    private String commandName;

    @Parameters(index = "1..*", description = "file name from which json is to be fetched")
    String[] files;

    @Override
    public void run() {
        for(String file:files) {
            try{
                parse(file);
            }catch (Exception ex) {
                System.out.println("Unable to parse with exception: "+ ex);
            }
        }
    }

    private void parse(String filePath)
            throws IOException {
        String input = Files.lines(Path.of(filePath)).collect(Collectors.joining());
        JSONLexer jsonLexer = new JSONLexer(CharStreams.fromString(input));
        jsonLexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(jsonLexer);
        JSONParser parser = new JSONParser(tokens);
        CustomErrorListener errorListener = new CustomErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        jsonLexer.addErrorListener(errorListener);
        ParseTree tree = parser.json();
        if(errorListener.hasError()) {
            System.out.println("Invalid Json");
        }
        else{
            System.out.println("Valid json");
        }
    }

}

