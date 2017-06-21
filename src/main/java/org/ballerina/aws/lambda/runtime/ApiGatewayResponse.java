package org.ballerina.aws.lambda.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * AWS API Gateway response definition.
 */
public class ApiGatewayResponse {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    public ApiGatewayResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
        this.body = body;
    }

    public ApiGatewayResponse(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
