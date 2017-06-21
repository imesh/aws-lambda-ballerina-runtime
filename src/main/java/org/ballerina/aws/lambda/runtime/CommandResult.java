package org.ballerina.aws.lambda.runtime;

/**
 * Command result definition.
 */
public class CommandResult {

    private int exitCode;
    private String output;

    public CommandResult(int exitCode, String output) {
        this.exitCode = exitCode;
        this.output = output;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }
}
