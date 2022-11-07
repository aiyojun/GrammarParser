package com.jpro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aiyojun
 */
public class Lexer {

    private interface Rule {
        boolean satisfy(String s);
    }

    public static class LexerNotifier {
        public void error(String s) {
            System.out.println("Unexpected string : " + s);
        }
    }

    private LexerNotifier lexerNotifier = new LexerNotifier();

    public void setLexerNotifier(LexerNotifier lexerNotifier) {
        this.lexerNotifier = lexerNotifier;
    }

    private final StringBuilder appender = new StringBuilder();

    private final List<Rule> rules = new ArrayList<>(128);
    private final List<String> rulesName = new ArrayList<>(128);

    public Lexer() {
        rules.add(s -> s.matches("^[a-zA-Z_]+[0-9a-zA-Z_]*"));
        rules.add(s -> s.matches("^(-)?[0-9]*(\\.)[0-9]+"));
        rules.add(s -> s.matches("^(-)?[0-9]+"));
        rules.add(s -> s.matches("^'.'"));
        rulesName.add("variable");
        rulesName.add("float");
        rulesName.add("integer");
        rulesName.add("char");

        mapper.put("+", 1);
        mapper.put("-", 2);
        mapper.put("*", 3);
        mapper.put("/", 4);
        mapper.put("%", 5);
        mapper.put("(", 6);
        mapper.put(")", 7);
        mapper.put("[", 201);
        mapper.put("]", 202);
        mapper.put("{", 8);
        mapper.put("}", 9);
        mapper.put("?", 200);
        mapper.put(",", 203);
        mapper.put(".", 204);
        mapper.put("string", 10);
        mapper.put("variable", 17);
        mapper.put("float", 18);
        mapper.put("integer", 19);
        mapper.put("char", 20);
        mapper.put("multi line comment", 101);
        mapper.put("single line comment", 101);
    }

    public String getTypeText(int type) {
        for (Map.Entry<String, Integer> pair : mapper.entrySet()) {
            if (pair.getValue().equals(type)) {
                return pair.getKey();
            }
        }
        throw new RuntimeException("Unknown type : " + type);
    }

    private String type;

    private String text;

    public String getText() {
        return text;
    }

    public interface LexerHandler {
        void handle(int type, String text);
    }

    private LexerHandler lexerHandler;

    public void setLexerHandler(LexerHandler lexerHandler) {
        this.lexerHandler = lexerHandler;
    }

    private final Map<String, Integer> mapper = new HashMap<>();

    private void setToken(String type, String text) {
        this.type = type;
        this.text = text;
        if (lexerHandler != null) {
            lexerHandler.handle(mapper.get(type), text);
        }
    }

    public void lex(InputStream inputStream) {

        char ch;

        boolean slash = false;

        while (true) {
            try {
                ch = (char) inputStream.read();

                if (ch == 'Q') {
                    break;
                }

                if (slash && ch == '/') {
                    while (true) {
                        ch = (char) inputStream.read();

                        if (ch == '\n') {
                            setToken("multi line comment", "//" + appender);
                            appender.delete(0, appender.length());
                            break;
                        }

                        appender.append(ch);
                    }
                    slash = false;
                    continue;
                }
                if (slash && ch == '*') {
                    boolean asterisk = false;
                    while (true) {
                        ch = (char) inputStream.read();

                        appender.append(ch);

                        if (asterisk && ch == '/') {
                            setToken("single line comment", "/*" + appender);
                            appender.delete(0, appender.length());
                            break;
                        }

                        asterisk = ch == '*';
                    }
                    slash = false;
                    continue;
                }

                if (slash) {
                    setToken("/", "/");
                }

                slash = ch == '/';

                // reduce

                if (ch == ' ' || ch == '\t' || ch == '\n'
                        || ch == '+'
                        || ch == '-'
                        || ch == '*'
                        || ch == '/'
                        || ch == '%'
                        || ch == '['
                        || ch == ']'
                        || ch == '{'
                        || ch == '}'
                        || ch == '('
                        || ch == ')'
                        || ch == ','
                        || ch == '.'
                        || ch == '?'
                        || ch == '"'
                ) {
                    boolean ok = false;

                    // reduce first

                    if (appender.length() > 0) {
                        for (int i = 0; i < rules.size(); i++) {
                            Rule rule = rules.get(i);
                            if (rule.satisfy(appender.toString())) {
                                setToken(rulesName.get(i), appender.toString());
                                appender.delete(0, appender.length());
                                ok = true;
                                break;
                            }
                        }

                        if (!ok) {
                            lexerNotifier.error(appender.toString());
                            appender.delete(0, appender.length());
                        }
                    }

                    // symbols processing

                    if (ch == '+'
                            || ch == '-'
                            || ch == '*'
                            || ch == '%'
                            || ch == '{'
                            || ch == '['
                            || ch == ']'
                            || ch == '}'
                            || ch == '('
                            || ch == ')'
                            || ch == ','
                            || ch == '.'
                            || ch == '?') {
                        String sym = String.format("%c", ch);
                        setToken(sym, sym);
                    }

                    if (ch == '"') {
                        boolean escape = false;

                        while (true) {

                            ch = (char) inputStream.read();

                            if (!escape && ch == '"') {
                                setToken("string", appender.toString());
                                appender.delete(0, appender.length());
                                break;
                            } else {
                                appender.append(ch);
                            }

                            escape = ch == '\\';
                        }
                    }
                } else {
                    appender.append(ch);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
