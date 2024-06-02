package org.vaibhav.Util;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkingDir {
    private Path currentDir;
    private static WorkingDir workingDir;
    private WorkingDir() {
        currentDir = Paths.get(System.getProperty("user.dir"));
    }
    public static WorkingDir getInstance() {
        if(workingDir == null) {
            workingDir = new WorkingDir();
        }
        return workingDir;
    }
    public Path getCurrentDir() {
        return currentDir;
    }
    public void changeDir(String newPath) {
        newPath = newPath.trim();
        if(newPath.equals("..")) {
            if(currentDir.getParent() != null) {
                currentDir = currentDir.getParent();
            }
        }
        else if(newPath.equals(".")) {
            // Do nothing
        }
        else if(newPath.startsWith("/")) {
            Path path = Path.of(newPath);
            if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("No Such file or directory");
                return;
            }
            currentDir = path;
        }
        else if(newPath.matches("[._\\-\\w\\d]+")){
            if(!Files.exists(Path.of(currentDir.toString(),newPath), LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("No Such file or directory");
                return;
            }
            currentDir = Path.of(currentDir.toString(),newPath);
        }
        else {
            System.out.println("No such file or Directory");
        }
    }
}
