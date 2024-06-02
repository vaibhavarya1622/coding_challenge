package org.vaibhav.Util;

import java.util.Objects;

public class Output {
    private String output;
    private static Output object;
    private Output() {
    }
    public static Output getInstance() {
        if(object == null) {
           object = new Output();
        }
        return object;
    }
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void printOutput() {
        if(!Objects.isNull(this.output) && !this.output.isEmpty())
            System.out.println(this.output);
    }
}
