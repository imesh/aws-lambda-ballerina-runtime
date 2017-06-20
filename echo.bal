import ballerina.lang.system;

function main(string[] args) {
    if (args.length == 0) {
        system:println({ "error": "No input was found"});
        return;
    }
    system:println(args[0]);
}
