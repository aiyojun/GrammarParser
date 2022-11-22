package com.jpro;

public class Token {
    /* We should provide the following functions:
     *   1. record raw token text
     *   2. type mark
     *   3. locate raw token, two expression: in whole file; in one line
     * */
    private TokenType type;
    private String text;
    private int start;
    private int end;
    private int lineNumber;
    private int lineStart;

    public static Token build(TokenType tokenType, String text) { return new Token(tokenType, text); }
    private Token(TokenType tokenType, String text) { this.type = tokenType; this.text = text; }
    public TokenType getType() { return type; }
    public void setType(TokenType type) { this.type = type; }
    public String getText() { return text; }
    public void setPosition(int start, int end) { this.start = start; this.end = end; }
    public void setText(String text) { this.text = text; }
    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }
    public int getEnd() { return end; }
    public void setEnd(int end) { this.end = end; }
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public int getLineStart() { return lineStart; }
    public void setLineStart(int lineStart) { this.lineStart = lineStart; }
}
