package com.jpro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    public record Pair<T, K>(T key, K value) {
        public static <T, K> Pair<T, K> build(T key, K value) {
            return new Pair<>(key, value);
        }
    }

    private static final List<Pair<Pattern, TokenType>> regexes = new ArrayList<>(1024);

    private static int reduce(Matcher m) {
        return m.lookingAt() ? m.group(0).length() : 0;
    }

    static {
        regexes.add(Pair.build(Pattern.compile("^[ \r\t]*"), TokenType.WHITESPACE));
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
        regexes.add(Pair.build(Pattern.compile("^&&"), TokenType.F_ADD));
        regexes.add(Pair.build(Pattern.compile("^&"), TokenType.B_AND));
        regexes.add(Pair.build(Pattern.compile("^\\|\\|"), TokenType.F_OR));
        regexes.add(Pair.build(Pattern.compile("^\\|"), TokenType.B_OR));
        regexes.add(Pair.build(Pattern.compile("^!"), TokenType.F_NOT));
        regexes.add(Pair.build(Pattern.compile("^\\?"), TokenType.F_WHY));
        regexes.add(Pair.build(Pattern.compile("^pub"), TokenType.K_PUB));
        regexes.add(Pair.build(Pattern.compile("^byte"), TokenType.K_BYTE));
        regexes.add(Pair.build(Pattern.compile("^string"), TokenType.K_STR));
        regexes.add(Pair.build(Pattern.compile("^char"), TokenType.K_CHAR));
        regexes.add(Pair.build(Pattern.compile("^int"), TokenType.K_INT));
        regexes.add(Pair.build(Pattern.compile("^float"), TokenType.K_FLOAT));
        regexes.add(Pair.build(Pattern.compile("^bool"), TokenType.K_BOOL));
        regexes.add(Pair.build(Pattern.compile("^def"), TokenType.K_DEF));
        regexes.add(Pair.build(Pattern.compile("^var"), TokenType.K_VAR));
        regexes.add(Pair.build(Pattern.compile("^val"), TokenType.K_VAL));
        regexes.add(Pair.build(Pattern.compile("^const"), TokenType.K_CONST));
        regexes.add(Pair.build(Pattern.compile("^return"), TokenType.K_RETURN));
        regexes.add(Pair.build(Pattern.compile("^void"), TokenType.K_VOID));
        regexes.add(Pair.build(Pattern.compile("^import"), TokenType.K_IMPORT));
        regexes.add(Pair.build(Pattern.compile("^class"), TokenType.K_CLASS));
        regexes.add(Pair.build(Pattern.compile("^true"), TokenType.K_TRUE));
        regexes.add(Pair.build(Pattern.compile("^false"), TokenType.K_FALSE));
        regexes.add(Pair.build(Pattern.compile("^if"), TokenType.K_IF));
        regexes.add(Pair.build(Pattern.compile("^else"), TokenType.K_ELSE));
        regexes.add(Pair.build(Pattern.compile("^match"), TokenType.K_MATCH));
        regexes.add(Pair.build(Pattern.compile("^case"), TokenType.K_CASE));
        regexes.add(Pair.build(Pattern.compile("^for"), TokenType.K_FOR));
        regexes.add(Pair.build(Pattern.compile("^continue"), TokenType.K_CONTINUE));
        regexes.add(Pair.build(Pattern.compile("^break"), TokenType.K_BREAK));
        regexes.add(Pair.build(Pattern.compile("^while"), TokenType.K_WHILE));
        regexes.add(Pair.build(Pattern.compile("^[a-zA-Z_]+\\w*"), TokenType.ID));
        regexes.add(Pair.build(Pattern.compile("^(-)?\\d*(\\.)\\d+"), TokenType.V_FLOAT));
        regexes.add(Pair.build(Pattern.compile("^(-)?\\d+"), TokenType.V_INT));
        regexes.add(Pair.build(Pattern.compile("^'.'"), TokenType.V_CHAR));
    }

    public List<Pair<String, TokenType>> lex(String text) {
        final List<Pair<String, TokenType>> tokens = new ArrayList<>(1024);
        String rest = text;
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
                        tokens.add(Pair.build(rest.substring(0, index + 1), TokenType.V_STR));
                        break;
                    }
                    escape = ch == '\\';
                }
                rest = rest.substring(index + 1);
                continue;
            }
            if (rest.startsWith("/*")) {
                rest = rest.substring(rest.indexOf("*/") + 2);
                continue;
            }
            boolean reduced = false;
            int reduceLength;
            for (Pair<Pattern, TokenType> regex : regexes) {
                reduceLength = reduce(regex.key().matcher(rest));
                if (reduceLength > 0) {
                    tokens.add(Pair.build(rest.substring(0, reduceLength), regex.value()));
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

    private static String readFile(String path) {
        StringBuilder appender = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(path), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                appender.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appender.toString();
    }

    public static void main(String[] args) {
        new Tokenizer().lex(readFile("/opt/Grammar/main.lea")).stream()
                .filter(p ->
                        p.value() != TokenType.COMMENT
                                && p.value() != TokenType.NEWLINE
                                && p.value() != TokenType.WHITESPACE
                )
                .forEach(stringTokenTypePair -> {
                    System.out.print(stringTokenTypePair.value().toString() + " ");
                });
    }

}
