# AWS Lambda Ballerina Runtime

Currently AWS Lambda only supports Node.js, Python, Java and C#. For enabling any other runtime on AWS Lambda 
requires a wrapper function implemented in one of the above languages. This project provides a Java wrapper 
function for supporting Ballerina functions on AWS Lambda.

## How to run

1. Clone this project:
   ```
   git clone https://github.com/imesh/aws-lambda-ballerina-runtime
   ```

2. Download Ballerina runtime distribution from ballerina.org:
   ```
   cd aws-lambda-ballerina-runtime
   wget http://ballerinalang.org/downloads/ballerina-runtime/ballerina-0.88.zip
   ```

3. Extract Ballerina runtime distribution in the project root folder:
   ```
   unzip ballerina-0.88.zip
   ```

4. Delete the Ballerina samples folder:
   ```
   rm -rf ballerina-0.88/samples/
   ```

5. Build project using Gradle:
   ```
   gradle build
   ```

6. Upload build/distributions/aws-lambda-ballerina-runtime.zip file to AWS Lambda.

7. Set handler name as follows and test:
   ```
   org.ballerina.lambda.sample.BallerinaFunctionInvoker::handleRequest
   ```
