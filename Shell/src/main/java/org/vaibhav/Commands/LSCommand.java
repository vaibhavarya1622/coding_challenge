package org.vaibhav.Commands;

import org.vaibhav.Util.Output;
import org.vaibhav.Util.WorkingDir;
import picocli.CommandLine.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Command(name = "ls", description = "This list the current directory contents")
public class LSCommand implements Runnable{
    @Override
    public void run() {
        WorkingDir dir = WorkingDir.getInstance();
        Output output = Output.getInstance();

        try{
            StringBuilder sb = new StringBuilder();
            Files.walkFileTree(dir.getCurrentDir(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                    String fileString = file.getFileName().toString();
                    sb.append(fileString).append("\n");
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path folder, BasicFileAttributes attr) {
                    String folderString = folder.getFileName().toString();
                    if(folderString.equals(dir.getCurrentDir().getFileName().toString())){
                        return FileVisitResult.CONTINUE;
                    }
                    sb.append(folderString).append("\n");
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
            output.setOutput(sb.toString());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
