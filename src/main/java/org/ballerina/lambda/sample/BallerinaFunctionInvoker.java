package org.ballerina.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Ballerina function invoker class.
 */
public class BallerinaFunctionInvoker {

    public String handleRequest(Map<String, Object> input, Context context) {
        System.out.println("Request received: " + input);
        CommandExecutor.executeCommand("cp", "-r", "ballerina", "/tmp/ballerina");
        CommandExecutor.executeCommand("cp", "hello.bal", "/tmp/hello.bal");

        Map<String, String> env = new HashMap<String, String>();
        env.put("JAVA_HOME", System.getProperty("java.home"));
        return CommandExecutor.executeCommand(env,"/tmp/ballerina/bin/ballerina", "run", "main",
                "/tmp/hello.bal", input.toString());
    }
}