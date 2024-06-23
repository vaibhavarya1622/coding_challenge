package org.vaibhav.Utils.SerializerDeserializer;

import java.util.ArrayList;
import java.util.List;
import static org.vaibhav.Utils.CONSTANTS.ARRAY;
import static org.vaibhav.Utils.CONSTANTS.BULK_STRING;
import static org.vaibhav.Utils.CONSTANTS.CRLF;
import static org.vaibhav.Utils.CONSTANTS.ERROR;
import static org.vaibhav.Utils.CONSTANTS.INTEGER;
import static org.vaibhav.Utils.CONSTANTS.STRING;

public class Deserializer {
    private Serializer serializer = new Serializer();

    public String[] parseRequest(String request) {
        String firstByte = request.substring(0,1);
        if(!firstByte.equals(ARRAY)) {
            throw new RuntimeException("Request should be a RESP Array");
        }
        return parseArray(request).toArray(new String[0]);
    }
    public List<String> parseArray(String request) {
        String[] arrayCommands = request.split(CRLF);
        List<String> commands = new ArrayList<>();
        for(int idx = 1;idx<arrayCommands.length;++idx) {
            if(arrayCommands[idx].startsWith(BULK_STRING)) {
                String bulkStringRequest = arrayCommands[idx]+CRLF+arrayCommands[idx+1] + CRLF;
                commands.add(parseBulkString(bulkStringRequest));
                ++idx;
            }
            else if(arrayCommands[idx].startsWith(ARRAY)) {
                int len = Integer.parseInt(arrayCommands[idx].substring(1));
                StringBuilder sb = new StringBuilder();
                while(len-->0) {
                    sb.append(arrayCommands[idx++]).append(CRLF);
                }
                commands.addAll(parseArray(sb.toString()));
                idx--;
            }
            else if(arrayCommands[idx].startsWith(INTEGER)) {
                commands.add(String.valueOf(parseInteger(arrayCommands[idx])));
            }
            else if(arrayCommands[idx].startsWith(STRING)) {
                commands.add(parseSimpleString(arrayCommands[idx]));
            }
            else if(arrayCommands[idx].startsWith(ERROR)) {
                commands.add(parseErrors(arrayCommands[idx]));
            }
        }
        return commands;
    }
    public String parseBulkString(String request) {
        String[] token = request.split(CRLF);
        String type = token[0].substring(0,1);
        if(!type.equals(BULK_STRING)) {
            throw new RuntimeException("Bulk String should start with $");
        }
        String value = token[1];
        return value;
    }
    public Integer parseInteger(String request) {
        String[] token = request.split(CRLF);
        String type = token[0].substring(0,1);
        String value = token[0].substring(1);
        return Integer.parseInt(value);
    }
    public String parseSimpleString(String request) {
        String[] token = request.split(CRLF);
        String type = token[0].substring(0,1);
        String value = token[0].substring(1);
        return value;
    }
    public String parseErrors(String request) {
        String[] token = request.split(CRLF);
        String type = token[0].substring(0,1);
        String value = token[0].substring(1);
        return value;
    }
}
