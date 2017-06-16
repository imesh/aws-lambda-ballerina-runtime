# AWS Lambda Ballerina Runtime

Currently, AWS Lambda only supports Node.js, Python, Java and C#. For enabling any other runtime on AWS Lambda 
a wrapper function is required to be implemented in one of the above languages. This project provides a Java wrapper 
function for supporting Ballerina functions on AWS Lambda.

## How to Run

1. Clone this project:
   ```
   git clone https://github.com/imesh/aws-lambda-ballerina-runtime
   ```

2. Download Ballerina runtime distribution from [ballerinalang.org](http://ballerinalang.org/):
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

7. Set the handler name as follows and execute a test:
   ```
   org.ballerina.lambda.sample.BallerinaFunctionInvoker::handleRequest
   ```
   
   ```
   START RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956 Version: $LATEST
   Request received: {key3=value3, key2=value2, key1=value1}
   Executing command: cp -r ballerina /tmp/ballerina
   Command executed in 25 ms.
   Executing command: cp hello.bal /tmp/hello.bal
   Command executed in 1 ms.
   Executing command: /tmp/ballerina/bin/ballerina run main /tmp/hello.bal {key3=value3, key2=value2, key1=value1}
   Output: {key3=value3, key2=value2, key1=value1}
   Command executed in 1898 ms.
   END RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956
   REPORT RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956	Duration: 1940.79 ms	Billed Duration: 2000 ms 	Memory Size: 1536 MB	Max          Memory Used: 138 MB	
   ```
   
## References
- [Scripting Languages for AWS Lambda: Running PHP, Ruby, and Go](https://aws.amazon.com/blogs/compute/scripting-languages-for-aws-lambda-running-php-ruby-and-go/)
   
## License
Apache 2.0
