package org.ballerina.lambda.runtime;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Ballerina function invoker class.
 */
public class BallerinaFunctionInvoker {

    private static final String BAL_FILE_NAME_ENV_VAR = "BAL_FILE_NAME";
    private static final String JAVA_HOME_ENV_VAR = "JAVA_HOME";
    private static final String JAVA_HOME_SYS_PROPERTY = "java.home";
    private static final String MESSAGE_BODY = "body";

    public String handleRequest(Map<String, Object> input, Context context) {
        // Check for BAL_FILE_NAME environment variable
        String balFileName = System.getenv(BAL_FILE_NAME_ENV_VAR);
        if (balFileName == null) {
            throw new RuntimeException("Environment variable " + BAL_FILE_NAME_ENV_VAR + " not defined");
        }

        System.out.println("Request received: " + input);

        // Copy ballerina runtime and function file to /tmp directory
        CommandExecutor.executeCommand("cp", "-r", "ballerina", "/tmp/ballerina");
        CommandExecutor.executeCommand("cp", balFileName, "/tmp/" + balFileName);

        Map<String, String> env = new HashMap<String, String>();
        env.put(JAVA_HOME_ENV_VAR, System.getProperty(JAVA_HOME_SYS_PROPERTY));

        // Prepare input string
        String inputStr = "";
        if (input != null) {
            if (input.containsKey(MESSAGE_BODY)) {
                inputStr = (String) input.get(MESSAGE_BODY);
            }
            System.out.println("Input string: " + inputStr);
        }

        // Execute ballerina function and return the result
        return CommandExecutor.executeCommand(env, "/tmp/ballerina/bin/ballerina",
                "run", "main", "/tmp/" + balFileName, inputStr);
    }
}