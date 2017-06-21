package org.ballerina.aws.lambda.runtime;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * AWS API Gateway Ballerina function invoker.
 */
public class ApiGatewayFunctionInvoker {

    private static final String BAL_FILE_NAME_ENV_VAR = "BAL_FILE_NAME";
    private static final String JAVA_HOME_ENV_VAR = "JAVA_HOME";
    private static final String JAVA_HOME_SYS_PROPERTY = "java.home";
    private static final String MESSAGE_BODY = "body";
    private static final Gson gson = new GsonBuilder().create();

    private final String balFileName;

    /***
     * Default constructor for initializing ballerina file name.
     */
    public ApiGatewayFunctionInvoker() {
        // Check for BAL_FILE_NAME environment variable
        balFileName = System.getenv(BAL_FILE_NAME_ENV_VAR);
        if (balFileName == null) {
            throw new RuntimeException("Environment variable " + BAL_FILE_NAME_ENV_VAR + " not defined");
        }
    }

    /***
     * Handle AWS API Gateway request
     * @param input Incoming request message
     * @param context Lambda context
     * @return API Gateway response
     */
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            LambdaLogger logger = context.getLogger();
            logger.log("Request received: " + input + System.lineSeparator());

            // Copy ballerina runtime and function file to /tmp directory
            CommandExecutor.executeCommand(logger, "cp", "-r", "ballerina", "/tmp/ballerina");
            CommandExecutor.executeCommand(logger, "cp", balFileName, "/tmp/" + balFileName);

            // Read message body from the input
            String body = "";
            if (input != null) {
                if (input.containsKey(MESSAGE_BODY)) {
                    body = gson.toJson(input.get(MESSAGE_BODY));
                }
                logger.log("Message body: " + body + System.lineSeparator());
            }

            // Execute ballerina function and return the response
            Map<String, String> env = new HashMap<String, String>();
            env.put(JAVA_HOME_ENV_VAR, System.getProperty(JAVA_HOME_SYS_PROPERTY));
            CommandResult result = CommandExecutor.executeCommand(logger, env, "/tmp/ballerina/bin/ballerina",
                    "run", "main", "/tmp/" + balFileName, body);
            int statusCode = (result.getExitCode() == 0) ? 200 : 500;
            return new ApiGatewayResponse(statusCode, result.getOutput());
        } catch (Exception e) {
            return new ApiGatewayResponse(500, e.getMessage());
        }
    }
}