package com.jpro;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final List<Pair<Pattern, TokenType>> regexes = new ArrayList<>(1024);
    private static final Map<String, TokenType> keywords = new HashMap<>(512);

    private static int reduce(Matcher m) {
        return m.lookingAt() ? m.group(0).length() : 0;
    }

    static {
        regexes.add(Pair.build(Pattern.compile("^[ \t]*"), TokenType.WHITESPACE));
        regexes.add(Pair.build(Pattern.compile("^\n"), TokenType.NEWLINE));
        regexes.add(Pair.build(Pattern.compile("^\\{"), TokenType.L_BRACE));
        regexes.add(Pair.build(Pattern.compile("^}"), TokenType.R_BRACE));
        regexes.add(Pair.build(Pattern.compile("^\\["), TokenType.L_SQUARE));
        regexes.add(Pair.build(Pattern.compile("^]"), TokenType.R_SQUARE));
        regexes.add(Pair.build(Pattern.compile("^\\("), TokenType.L_PAREN));
        regexes.add(Pair.build(Pattern.compile("^\\)"), TokenType.R_PAREN));
        regexes.add(Pair.build(Pattern.compile("^=>"), TokenType.F_ARROW));
        regexes.add(Pair.build(Pattern.compile("^=="), TokenType.F_EQ));
        regexes.add(Pair.build(Pattern.compile("^="), TokenType.F_ASSIGN));
        regexes.add(Pair.build(Pattern.compile("^>="), TokenType.F_GE));
        regexes.add(Pair.build(Pattern.compile("^<="), TokenType.F_LE));
        regexes.add(Pair.build(Pattern.compile("^>"), TokenType.F_GT));
        regexes.add(Pair.build(Pattern.compile("^<"), TokenType.F_LT));
        regexes.add(Pair.build(Pattern.compile("^:"), TokenType.F_COLON));
        regexes.add(Pair.build(Pattern.compile("^;"), TokenType.F_SEMI));
        regexes.add(Pair.build(Pattern.compile("^,"), TokenType.F_COMMA));
        regexes.add(Pair.build(Pattern.compile("^\\."), TokenType.F_DOT));
        regexes.add(Pair.build(Pattern.compile("^\\+\\+"), TokenType.F_INC));
        regexes.add(Pair.build(Pattern.compile("^\\+"), TokenType.F_ADD));
        regexes.add(Pair.build(Pattern.compile("^--"), TokenType.F_DEC));
        regexes.add(Pair.build(Pattern.compile("^-"), TokenType.F_SUB));
        regexes.add(Pair.build(Pattern.compile("^\\*"), TokenType.F_MUL));
        regexes.add(Pair.build(Pattern.compile("^//(.)*\n"), TokenType.COMMENT));
        regexes.add(Pair.build(Pattern.compile("^/"), TokenType.F_DIV));
        regexes.add(Pair.build(Pattern.compile("^%"), TokenType.F_MOD));
        regexes.add(Pair.build(Pattern.compile("^~"), TokenType.B_INV));
        regexes.add(Pair.build(Pattern.compile("^&&"), TokenType.F_AND));
        regexes.add(Pair.build(Pattern.compile("^&"), TokenType.B_AND));
        regexes.add(Pair.build(Pattern.compile("^\\|\\|"), TokenType.F_OR));
        regexes.add(Pair.build(Pattern.compile("^\\|"), TokenType.B_OR));
        regexes.add(Pair.build(Pattern.compile("^!="), TokenType.F_NE));
        regexes.add(Pair.build(Pattern.compile("^!"), TokenType.F_NOT));
        regexes.add(Pair.build(Pattern.compile("^\\?"), TokenType.F_WHY));
        regexes.add(Pair.build(Pattern.compile("^[a-zA-Z_]+\\w*"), TokenType.ID));
        regexes.add(Pair.build(Pattern.compile("^(-)?\\d*(\\.)\\d+"), TokenType.V_FLOAT));
        regexes.add(Pair.build(Pattern.compile("^(-)?\\d+"), TokenType.V_INT));
        regexes.add(Pair.build(Pattern.compile("^'.'"), TokenType.V_CHAR));
        keywords.put("pub", TokenType.K_PUB);
        keywords.put("byte", TokenType.K_BYTE);
        keywords.put("string", TokenType.K_STR);
        keywords.put("char", TokenType.K_CHAR);
        keywords.put("int", TokenType.K_INT);
        keywords.put("float", TokenType.K_FLOAT);
        keywords.put("bool", TokenType.K_BOOL);
        keywords.put("def", TokenType.K_DEF);
        keywords.put("var", TokenType.K_VAR);
        keywords.put("val", TokenType.K_VAL);
        keywords.put("const", TokenType.K_CONST);
        keywords.put("return", TokenType.K_RETURN);
        keywords.put("void", TokenType.K_VOID);
        keywords.put("import", TokenType.K_IMPORT);
        keywords.put("class", TokenType.K_CLASS);
        keywords.put("true", TokenType.K_TRUE);
        keywords.put("false", TokenType.K_FALSE);
        keywords.put("if", TokenType.K_IF);
        keywords.put("else", TokenType.K_ELSE);
        keywords.put("match", TokenType.K_MATCH);
        keywords.put("case", TokenType.K_CASE);
        keywords.put("for", TokenType.K_FOR);
        keywords.put("continue", TokenType.K_CONTINUE);
        keywords.put("break", TokenType.K_BREAK);
        keywords.put("while", TokenType.K_WHILE);
        keywords.put("this", TokenType.K_THIS);
        keywords.put("null", TokenType.K_NULL);
        keywords.put("sync", TokenType.K_SYNC);
        keywords.put("await", TokenType.K_AWAIT);
    }

    public List<Token> lex(String text) {
        final List<Token> tokens = new ArrayList<>(1024);
        String rest = text;
        int accumulate = 0;
        int lineNumber = 0;
        int numberOfLine = 0;

        String tokenText;
        TokenType tokenType;
        while (!rest.isEmpty()) {
            if (rest.charAt(0) == '"') {
                boolean escape = false;
                int index = 0;
                while (true) {
                    char ch = rest.charAt(++index);
                    if (ch == '\n' || ch == '\r') {
                        throw new RuntimeException("Unexpected \033[31;1mnewline in string\033[0m");
                    }
                    if (!escape && ch == '"') {
                        tokenText = rest.substring(0, index + 1);
                        tokenType = TokenType.V_STR;
                        tokens.add(Token.build(tokenType, tokenText, accumulate, accumulate + tokenText.length(), lineNumber, numberOfLine));
                        numberOfLine += tokenText.length();
                        accumulate += tokenText.length();
                        break;
                    }
                    escape = ch == '\\';
                }
                rest = rest.substring(index + 1);
                continue;
            }
            if (rest.startsWith("/*")) {
                int end = rest.indexOf("*/");
                tokenText = rest.substring(0, end + 2);
                tokenType = TokenType.COMMENT;
                tokens.add(Token.build(tokenType, tokenText, accumulate, accumulate + tokenText.length(), lineNumber, numberOfLine));
                String[] lines = tokenText.split("\n");
                lineNumber += lines.length - 1;
                numberOfLine = lines[lines.length - 1].length();
                accumulate += tokenText.length();
                rest = rest.substring(end + 2);
                continue;
            }
            boolean reduced = false;
            int reduceLength;
            for (Pair<Pattern, TokenType> regex : regexes) {
                reduceLength = reduce(regex.key().matcher(rest));
                if (reduceLength > 0) {
                    if (regex.value() == TokenType.ID && keywords.containsKey(rest.substring(0, reduceLength))) {
                        tokenText = rest.substring(0, reduceLength);
                        tokenType = keywords.get(rest.substring(0, reduceLength));
                        tokens.add(Token.build(tokenType, tokenText, accumulate, accumulate + tokenText.length(), lineNumber, numberOfLine));
                    } else {
                        tokenText = rest.substring(0, reduceLength);
                        tokenType = regex.value();
                        tokens.add(Token.build(tokenType, tokenText, accumulate, accumulate + tokenText.length(), lineNumber, numberOfLine));
                    }
                    if (regex.value() == TokenType.NEWLINE || regex.value() == TokenType.COMMENT) {
                        lineNumber++;
                        numberOfLine = 0;
                    } else {
                        numberOfLine += tokenText.length();
                    }
                    accumulate += reduceLength;
                    rest = rest.substring(reduceLength);
                    reduced = true;
                    break;
                }
            }
            if (!reduced) {
                throw new RuntimeException(String.format("Unexpected \033[31;1m%s\033[0m\n", rest.split("\n")[0]));
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        String context = Helper.readFile("/opt/jpro/Grammar/main.lea");
        new Tokenizer().lex(context).stream()
                .filter(token ->
                        token.getType() != TokenType.WHITESPACE
                                && token.getType() != TokenType.NEWLINE
                                && token.getType() != TokenType.COMMENT
                )
                .forEach(token -> {
                    System.out.printf("%s %s%n", token, context.substring(token.getStart(), token.getEnd()));
                });
    }

}