package org.vaibhav.Commands;

import org.vaibhav.Util.Output;
import org.vaibhav.Util.WorkingDir;
import picocli.CommandLine.*;

@Command(name = "pwd", description = "This gives the current working directory")
public class PWDCommand implements Runnable{
    @Override
    public void run() {
        WorkingDir dir = WorkingDir.getInstance();
        Output output = Output.getInstance();
        output.setOutput(dir.getCurrentDir().toString());
    }
}
