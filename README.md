# AWS Lambda Ballerina Runtime

Currently, AWS Lambda only supports Node.js, Python, Java and C#. For enabling any other runtime on AWS Lambda 
a wrapper function is required to be implemented in one of the above languages. This project provides a Java wrapper 
function for supporting Ballerina functions on AWS Lambda.

## How to Run

1. Clone this project:
   
   ```bash
   $ git clone https://github.com/imesh/aws-lambda-ballerina-runtime
   ```

2. Download and extract Ballerina runtime distribution:
   
   ```bash
   $ cd aws-lambda-ballerina-runtime
   $ wget http://ballerinalang.org/downloads/ballerina-runtime/ballerina-<version>.zip
   $ unzip ballerina-<version>.zip
   ```

3. Remove version from the Ballerina folder name and delete the samples folder:
   
   ```bash
   $ mv ballerina-<version>/ ballerina/
   $ rm -rf ballerina/samples/
   ```

4. Copy the Ballerina main function file to the project root folder:
   
   ```bash
   $ cp /path/of/bal/file/ /path/to/aws-lambda-ballerina-runtime/
   ```
   
   For an example if the Ballerina file name is "function.bal", the project folder may look as follows:
   
   ```bash
   $ ls
   README.md     ballerina/    build/        build.gradle  function.bal     src/
   ```

5. Build project using Gradle:
   
   ```bash
   gradle build
   ```

6. Upload build/distributions/aws-lambda-ballerina-runtime.zip file to AWS Lambda.

7. Set the handler name as follows and execute a test:
   
   ```
   org.ballerina.lambda.runtime.BallerinaFunctionInvoker::handleRequest
   ```
   
   ```bash
   START RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956 Version: $LATEST
   Request received: {key3=value3, key2=value2, key1=value1}
   Executing command: cp -r ballerina /tmp/ballerina
   Command executed in 25 ms.
   Executing command: cp function.bal /tmp/function.bal
   Command executed in 1 ms.
   Executing command: /tmp/ballerina/bin/ballerina run main /tmp/function.bal {key3=value3, key2=value2, key1=value1}
   Output: {key3=value3, key2=value2, key1=value1}
   Command executed in 1898 ms.
   END RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956
   REPORT RequestId: 4f655bb9-52cf-11e7-b297-0b19a4fd7956	Duration: 1940.79 ms	Billed Duration: 2000 ms	Memory Size: 1536 MB	Max Memory Used: 138 MB	
   ```
   
## References
- [Scripting Languages for AWS Lambda: Running PHP, Ruby, and Go](https://aws.amazon.com/blogs/compute/scripting-languages-for-aws-lambda-running-php-ruby-and-go/)
   
## License
Apache 2.0
