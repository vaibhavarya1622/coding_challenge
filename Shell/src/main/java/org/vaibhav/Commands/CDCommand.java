package org.vaibhav.Commands;

import org.vaibhav.Util.Output;
import org.vaibhav.Util.WorkingDir;
import picocli.CommandLine.*;

@Command(name = "cd", description = "this command is used to change directory")
public class CDCommand implements Runnable{
    @Parameters(description = "the cd arguments. Could be relative or absolute path string")
    private String args;

    @Override
    public void run() {
        WorkingDir dir = WorkingDir.getInstance();
        Output output = Output.getInstance();
        output.setOutput("");
        dir.changeDir(args);
    }
}
