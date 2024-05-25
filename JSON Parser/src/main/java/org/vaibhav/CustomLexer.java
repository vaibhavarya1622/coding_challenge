package org.vaibhav;

import java.util.Iterator;

public class CustomLexer
        implements Iterator<Token> {
    private final String input;
    private int pos;
    private char cur_char;
    public CustomLexer(String input) {
        input += Character.MIN_VALUE;
        this.input = input;
        pos = 0;
        cur_char = input.charAt(pos);
    }
    private Token Number() {
        long value = 0;
        while(pos<input.length() && Character.isDigit(cur_char)) {
            value = value*10+ (cur_char-'0');
            ++pos;
            cur_char = input.charAt(pos);
        }
        return new Token(Type.NUMBER,String.valueOf(value));
    }
    private Token String() {
        StringBuilder sb = new StringBuilder();
        while(pos<input.length() &&  String.valueOf(cur_char).matches("[a-zA-Z0-9-_\\s\"]")) {
            sb.append(cur_char);
            ++pos;
            cur_char = input.charAt(pos);
        }
        return new Token(Type.STRING,sb.toString().trim());
    }
    private Token NULL() {
        for(int i=0;i<4;++i) {
            cur_char = input.charAt(++pos);
        }
        return new Token(Type.NULL,Type.NULL.name());
    }

    private Token LBRACE() {
        cur_char = input.charAt(++pos);
        return new Token(Type.LBRACE,"{");
    }

    private Token RBRACE() {
        cur_char = input.charAt(++pos);
        return new Token(Type.RBRACE,"}");
    }
    private Token LBRACKET() {
        cur_char = input.charAt(++pos);
        return new Token(Type.LBRACKET,"[");
    }
    private Token RBRACKET() {
        cur_char = input.charAt(++pos);
        return new Token(Type.RBRACKET,"]");
    }
    private void skipWhiteSpace() {
        while (pos<input.length() && Character.isWhitespace(cur_char)) {
            ++pos;
            cur_char = input.charAt(pos);
        }
    }
    private Token COLON() {
        cur_char = input.charAt(++pos);
        return new Token(Type.COLON,":");
    }
    private Token COMMA() {
        cur_char = input.charAt(++pos);
        return new Token(Type.COMMA, ",");
    }
    private Token TRUE() {
        for(int i=0;i<4;++i) {
            cur_char = input.charAt(++pos);
        }
        return new Token(Type.TRUE, Type.TRUE.name());
    }
    private Token FALSE() {
        for(int i=0;i<5;++i) {
            cur_char = input.charAt(++pos);
        }
        return new Token(Type.FALSE, Type.FALSE.name());
    }
    private void EOF() {
        this.pos = input.length();
        this.cur_char = Character.MIN_VALUE;
    }
    @Override
    public boolean hasNext() {
        return cur_char != Character.MIN_VALUE;
    }
    @Override
    public Token next() {
        while (cur_char != Character.MIN_VALUE) {
            if(Character.isWhitespace(cur_char)) {
                skipWhiteSpace();
            }
            else if(Character.isDigit(cur_char)) {
                return Number();
            }
            else if(":".equals(String.valueOf(cur_char))) {
                return COLON();
            }
            else if("{".equals(String.valueOf(cur_char))) {
                return LBRACE();
            }
            else if("}".equals(String.valueOf(cur_char))) {
                return RBRACE();
            }
            else if(",".equals(String.valueOf(cur_char))) {
                return COMMA();
            }
            else if("[".equals(String.valueOf(cur_char))) {
                return LBRACKET();
            }
            else if("]".equals(String.valueOf(cur_char))) {
                return RBRACKET();
            }
            else if("\"".equals(String.valueOf(cur_char))) {
                return String();
            }
            else if(input.substring(pos,pos+4).equals("true")) {
                return TRUE();
            }
            else if(input.substring(pos,pos+5).equals("false")) {
                return FALSE();
            }
            else if(input.substring(pos).startsWith("null")) {
                return NULL();
            }
            else{
                throw new IllegalArgumentException("Invalid character:\" "+cur_char+ " \" at position: "+pos);
            }
        }
        return null;
    }
}
