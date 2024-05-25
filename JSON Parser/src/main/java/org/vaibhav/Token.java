package org.vaibhav;

public class Token {
    private final Type type;
    private final String value;
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
    public Type getType() {
        return this.type;
    }

}
