package org.vaibhav;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Command(name = "ccwc",description = "this tool is used for word, line, character, and byte count")
public class wcCommand implements Runnable{
    @Parameters(index = "0", description = "The command name")
    private String commandName;
    @Option(names = {"-c"})
    private boolean byteCount;
    @Option(names = {"-l"})
    private boolean linesCount;
    @Option(names = {"-w"})
    private boolean wordCount;
    @Option(names = {"-m"})
    private boolean charCount;
    @Parameters(index = "1..*",description = "text from where text is to read")
    private String[] files;

    @Override
    public void run() {
        if(byteCount){
            try {
                for(String file:files) {
                    System.out.println(countBytes(file) + " " + file);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found.......");
            }
            catch (IOException e) {
                System.out.println("There was a problem reading this file");
            }
        }
        else if(linesCount){
            try {
                for(String file:files){
                    System.out.println(countLines(file)+ " " + file);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found.......");
            }
            catch (IOException e) {
                System.out.println("There was a problem reading this file");
            }
        }
        else if(wordCount) {
            try{
                for(String file:files) {
                    System.out.println(countWords(file)+" "+file);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found.......");
            }
            catch (IOException e) {
                System.out.println("There was a problem reading this file");
            }
        }
        else if(charCount){
            try{
                for(String file:files){
                    System.out.println(countChars(file)+" "+file);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found.......");
            }
            catch (IOException e) {
                System.out.println("There was a problem reading this file");
            }
        }
        else{
            try{
                for(String file: files){
                    System.out.println(countBytes(file)+" "+countLines(file)+" "+countWords(file)+" "+file);
                }
            }
            catch (NoSuchFileException e) {
                System.out.println("File not found.......");
            }
            catch (IOException e) {
                System.out.println("There was a problem reading this file");
            }
        }
    }

    private int countBytes(String filePath)
            throws IOException {
        String input = Files.lines(Path.of(filePath)).collect(Collectors.joining());
        return input.getBytes(StandardCharsets.UTF_8).length;
    }

    private int countLines(String filePath)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int count = 0;
        String line;
        while((line=reader.readLine())!=null) {
            ++count;
        }
        return count;
    }
    private int countWords(String filePath)
            throws IOException {
        return (int) Files.lines(Path.of(filePath)).map(l->l.split("\\s"))
                          .flatMap(Arrays::stream).filter(l-> !l.trim().isEmpty()).count();
    }
    private int countChars(String filePath)
            throws IOException {
        return (int)Files.lines(Path.of(filePath)).flatMap(line->line.chars().mapToObj(c->(char)c)).count();
    }
}
