/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerina.aws.lambda.runtime;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * AWS API Gateway Ballerina function invoker.
 */
public class ApiGatewayFunctionInvoker implements RequestStreamHandler {

    private static final String BAL_FILE_NAME_ENV_VAR = "BAL_FILE_NAME";
    private static final String JAVA_HOME_ENV_VAR = "JAVA_HOME";
    private static final String JAVA_HOME_SYS_PROPERTY = "java.home";
    private static final String MESSAGE_BODY = "body";

    private final JSONParser parser = new JSONParser();
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
     * Handle API Gateway request.
     * @param inputStream request message stream.
     * @param outputStream response message stream.
     * @param context Lambda context.
     * @throws IOException
     */
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        try {
            // Read request message
            JSONObject requestJson = (JSONObject) parser.parse(reader);
            logger.log("Request received: " + requestJson.toJSONString() + System.lineSeparator());

            // Copy ballerina runtime and function file to /tmp directory
            CommandExecutor.executeCommand(logger, "cp", "-r", "ballerina", "/tmp/ballerina");
            CommandExecutor.executeCommand(logger, "cp", balFileName, "/tmp/" + balFileName);

            // Execute ballerina function and return the response
            Map<String, String> env = new HashMap<String, String>();
            env.put(JAVA_HOME_ENV_VAR, System.getProperty(JAVA_HOME_SYS_PROPERTY));
            String body = requestJson.get(MESSAGE_BODY).toString();
            CommandResult result = CommandExecutor.executeCommand(logger, env, "/tmp/ballerina/bin/ballerina",
                    "run", "main", "/tmp/" + balFileName, body);

            // Prepare response message
            int statusCode = (result.getExitCode() == 0) ? 200 : 500;
            JSONObject headerJson = new JSONObject();
            JSONObject responseBody = (JSONObject) parser.parse(result.getOutput());

            responseJson.put("statusCode", statusCode);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());
        } catch (ParseException e) {
            responseJson.put("statusCode", "400");
            responseJson.put("exception", e);
        }

        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }
}