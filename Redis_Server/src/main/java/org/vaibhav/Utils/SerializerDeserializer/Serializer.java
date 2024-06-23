package org.vaibhav.Utils.SerializerDeserializer;


import static org.vaibhav.Utils.CONSTANTS.BULK_STRING;
import static org.vaibhav.Utils.CONSTANTS.CRLF;
import static org.vaibhav.Utils.CONSTANTS.ERROR;
import static org.vaibhav.Utils.CONSTANTS.INTEGER;
import static org.vaibhav.Utils.CONSTANTS.STRING;

public class Serializer {
    public String parseBulkString(String request) {
        return BULK_STRING+ request.length() + CRLF + request + CRLF;
    }
    public String parseInteger(Integer num) {
        return INTEGER + num + CRLF;
    }
    public String parseSimpleString(String request) {
        return STRING + request + CRLF;
    }
    public String parseError(String msg) {
        return ERROR + msg + CRLF;
    }
    public String parseNull() {
        return BULK_STRING+"-1"+CRLF;
    }
}
