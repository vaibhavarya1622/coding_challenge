package org.vaibhav;

import java.util.Objects;
import java.util.Stack;

public class CustomParser {
    private CustomLexer customLexer;
    private Token currentToken;
    public CustomParser(CustomLexer customLexer) {
        this.customLexer = customLexer;
    }
    Stack<Type> typeStack = new Stack<>();
    public void parse() {
        currentToken = customLexer.next();
        if(Objects.isNull(currentToken)) {
            throw new RuntimeException("Invalid json. It is empty");
        }
        checkValue();
    }
    private void checkValue() {
        if(!customLexer.hasNext()) {
            return;
        }
        switch (currentToken.getType()) {
            case STRING -> checkString();
            case NUMBER -> checkNumber();
            case LBRACE -> checkObj();
            case LBRACKET -> checkArray();
            case TRUE -> checkTrue();
            case FALSE -> checkFalse();
            case NULL -> checkNull();
        }
    }
    private void checkObj() {
        check(Type.LBRACE);
        if(currentToken.getType() == Type.RBRACE) {
            check(Type.RBRACE);
            return;
        }
        checkPair();
        while(currentToken.getType() == Type.COMMA) {
            check(Type.COMMA);
            checkPair();
        }
        check(Type.RBRACE);
    }
    private void checkPair() {
        checkString();
        check(Type.COLON);
        checkValue();
    }
    private void checkArray() {
        check(Type.LBRACKET);
        if(currentToken.getType() == Type.RBRACKET) {
            check(Type.RBRACKET);
            return;
        }
        checkValue();
        while(currentToken.getType() == Type.COMMA) {
            check(Type.COMMA);
            checkValue();
        }
        check(Type.RBRACKET);
    }
    private void checkNull() {
        if(!currentToken.getValue().matches("(?i)NULL")) {
            throw new RuntimeException(currentToken.getValue() +" invalid Null");
        }
        check(Type.NULL);
    }
    private void checkFalse() {
        if(!currentToken.getValue().matches("(?i)FALSE")) {
            throw new RuntimeException(currentToken.getValue() +" invalid False");
        }
        check(Type.FALSE);
    }
    private void checkTrue() {
        if(!currentToken.getValue().matches("(?i)TRUE")) {
            throw new RuntimeException(currentToken.getValue() +" invalid True");
        }
        check(Type.TRUE);
    }
    private void checkNumber() {
        if(!currentToken.getValue().matches("-?\\d+")) {
            throw new RuntimeException(currentToken.getValue() +" invalid Number");
        }
        check(Type.NUMBER);
    }
    private void checkString() {
        if(!currentToken.getValue().matches("^\"\\w+-?\\s*\\w+\"")) {
            throw new RuntimeException(currentToken.getValue() +" invalid String");
        }
        check(Type.STRING);
    }
    private void check(Type target) {
         if(currentToken.getType() != target) {
             throw new RuntimeException(target.name() +" not matched");
         }
         if(customLexer.hasNext())
            currentToken = customLexer.next();
    }
}
