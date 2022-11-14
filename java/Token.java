package com.jpro;

public class Token {
    private TokenType type;
    private String text;
    public static Token build(TokenType tokenType, String text) { return new Token(tokenType, text); }
    private Token(TokenType tokenType, String text) { this.type = tokenType; this.text = text; }
    public TokenType getType() { return type; }
    public void setType(TokenType type) { this.type = type; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
