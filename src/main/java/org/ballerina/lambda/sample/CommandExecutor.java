package org.ballerina.lambda.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Command executor utility class.
 */
public class CommandExecutor {
    public static String executeCommand(String... command) {
        return executeCommand(null, command);
    }

    public static String executeCommand(Map<String, String> env, String... command) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Executing command: " + formatCommand(command));
            ProcessBuilder pb = new ProcessBuilder(command);
            if(env != null) {
                Map<String, String> environment = pb.environment();
                for (String key : env.keySet()) {
                    environment.put(key, env.get(key));
                }
            }

            Process process = pb.start();
            String output = loadStream(process.getInputStream());
            String error = loadStream(process.getErrorStream());
            process.waitFor();

            if (isNotNullOrEmpty(output)) {
                System.out.println("Output: " + output.trim());
            }
            if (isNotNullOrEmpty(error)) {
                System.out.println("Error: " + error.trim());
            }
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            System.out.println("Command executed in " + duration + " ms.");
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String formatCommand(String... command) {
        StringBuffer sb = new StringBuffer();
        String delimiter = "";
        for (String val : command) {
            sb.append(delimiter).append(val);
            delimiter = " ";
        }
        return sb.toString();
    }

    private static String loadStream(InputStream s) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line).append(System.lineSeparator());
        return sb.toString();
    }

    private static boolean isNotNullOrEmpty(String value) {
        return (value != null) && (value.trim().length() > 0);
    }

}
