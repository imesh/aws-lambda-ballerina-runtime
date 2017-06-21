import ballerina.lang.system;

function main(string[] args) {
    if (args.length == 0) {
        json error = { "error": "No input was found" };
        system:println(error);
        return;
    }
    system:println(args[0]);
}
