package org.ballerina.lambda.runtime;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Ballerina function invoker class.
 */
public class BallerinaFunctionInvoker {

    private static final String BAL_FILE_NAME = "BAL_FILE_NAME";
    private static final String JAVA_HOME = "JAVA_HOME";

    public String handleRequest(Map<String, Object> input, Context context) {
        String balFileName = System.getenv(BAL_FILE_NAME);
        if(balFileName == null) {
            throw new RuntimeException("Environment variable " + BAL_FILE_NAME + " not defined");
        }

        System.out.println("Request received: " + input);

        CommandExecutor.executeCommand("cp", "-r", "ballerina", "/tmp/ballerina");
        CommandExecutor.executeCommand("cp", balFileName, "/tmp/" + balFileName);

        Map<String, String> env = new HashMap<String, String>();
        env.put(JAVA_HOME, System.getProperty("java.home"));
        String inputStr = (input != null) ? input.toString() : "";
        return CommandExecutor.executeCommand(env, "/tmp/ballerina/bin/ballerina",
                "run", "main", "/tmp/" + balFileName, inputStr);
    }
}