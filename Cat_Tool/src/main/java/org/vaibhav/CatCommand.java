package org.vaibhav;

import picocli.CommandLine.*;
import picocli.CommandLine.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Command(name = "cccat", description = "unix cat command")
public class CatCommand implements Runnable {
    private static final Logger log = Logger.getLogger(CatCommand.class.getName());
    @Option(names = {"-n", "-N","-b","-B"})
    private boolean showLines;
    @Parameters(index = "0..*",description = "text from where text is to be read")
    private String[] files;
    @Override
    public void run() {
        int counter = 1;
        if(files == null || files.length == 0 || files[0].equals("-")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String input;
                while((input = br.readLine()) != null) {
                    if(showLines){
                        System.out.print(counter++ + " ");
                    }
                    System.out.println(input);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for(String file:files) {
            try(Stream<String> stream = Files.lines(Path.of(file))) {
                List<String> input = stream.toList();
                for(String str: input) {
                    if(showLines){
                        System.out.print(counter++ + " ");
                    }
                    System.out.println(str);
                }
            }
            catch (IOException e) {
                log.info("File not found....");
                e.printStackTrace();
            }
        }
    }
}
