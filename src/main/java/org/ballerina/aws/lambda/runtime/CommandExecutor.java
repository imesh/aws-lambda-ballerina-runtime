package org.ballerina.aws.lambda.runtime;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Command executor utility class.
 */
public class CommandExecutor {

    /***
     * Executes a shell command using the process builder.
     * @param logger Lambda logger.
     * @param command The command to be executed with arguments.
     * @return Returns the result of the process execution captured via STDOUT or STDERR.
     */
    public static CommandResult executeCommand(LambdaLogger logger, String... command) {
        return executeCommand(logger, null, command);
    }

    /***
     * Executes a shell command using the process builder.
     *
     * @param logger Lambda logger.
     * @param env Environment variables to be injected to the process.
     * @param command The command to be executed with arguments.
     * @return Returns the result of the process execution captured via STDOUT or STDERR.
     */
    public static CommandResult executeCommand(LambdaLogger logger, Map<String, String> env, String... command) {
        try {
            long startTime = System.currentTimeMillis();
            logger.log("Executing command: " + formatCommand(command) + System.lineSeparator());
            ProcessBuilder pb = new ProcessBuilder(command);
            if (env != null) {
                Map<String, String> environment = pb.environment();
                for (String key : env.keySet()) {
                    environment.put(key, env.get(key));
                }
            }

            Process process = pb.start();
            String output = loadStream(process.getInputStream());
            String error = loadStream(process.getErrorStream());
            int exitCode = process.waitFor();

            if (isNotNullOrEmpty(output)) {
                logger.log("Output: " + output.trim() + System.lineSeparator());
            }
            if (isNotNullOrEmpty(error)) {
                logger.log("Error: " + error.trim() + System.lineSeparator());
            }
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            logger.log("Command executed in " + duration + " ms, exit code: " + exitCode + System.lineSeparator());
            return new CommandResult(exitCode, output);
        } catch (IOException e) {
            logger.log(getStackTraceAsString(e) + System.lineSeparator());
            return new CommandResult(-1, e.getMessage());
        } catch (InterruptedException e) {
            logger.log(getStackTraceAsString(e) + System.lineSeparator());
            return new CommandResult(-1, e.getMessage());
        }
    }

    private static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private static String formatCommand(String... command) {
        StringBuffer sb = new StringBuffer();
        String delimiter = "";
        for (String val : command) {
            if (isNotNullOrEmpty(val)) {
                sb.append(delimiter).append(val);
                delimiter = " ";
            }
        }
        return sb.toString();
    }

    private static String loadStream(InputStream s) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static boolean isNotNullOrEmpty(String value) {
        return (value != null) && (value.trim().length() > 0);
    }
}
