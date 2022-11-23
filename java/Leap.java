package com.jpro;

import java.util.List;
import java.util.Objects;

public class Leap {
    public record MergeTree(Node<Token> node, int from) {}

    public static void main(String[] args) {
        try {
            List<Token> tokens =
                    new Tokenizer().lex(Helper.readFile("/opt/jpro/Grammar/main.lea")).stream()
                            .filter(token ->
                                    token.getType() != TokenType.COMMENT &&
                                            token.getType() != TokenType.WHITESPACE
                            )
                            .peek(token -> {
                                if (token.getType() == TokenType.V_STR) {
                                    token.setText(token.getText().substring(1, token.getText().length() - 1));
                                }
                            })
//                            .map(token -> Token.build(token.getType(), token.getText()))
                            .toList();
            tokens.forEach(token -> System.out.printf("%s ", token.getType()));
            System.out.println();
            Leap leap = new Leap();
            MergeTree mergeTree = leap.pProgram(tokens);
            System.out.println(Helper.jsonTree(Helper.adjust(mergeTree.node()), 0));
            System.out.println("stop at : " + mergeTree.from());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public MergeTree pProgram(List<Token> context) {
        int from = 0;

        skipEndMark(context, from);

        TokenType[] exp = new TokenType[]{TokenType.K_IMPORT, TokenType.K_CLASS};
        String nodeSign = Thread.currentThread().getStackTrace()[1].getMethodName().substring(1);
        expect(nodeSign, context, from, exp);

        Node<Token> selfNode = Helper.buildNode(TokenType.PROGRAM, nodeSign);

        Token firstToken = context.get(from);
        while (firstToken.getType() == TokenType.K_IMPORT) {
            MergeTree importTree = pImportDeclaration(context, from);
            selfNode.addChild(importTree.node());
//            System.out.println(" > " + context.get(importTree.from()).getText());
            from = skipEndMark(context, importTree.from(), () -> expect(nodeSign, context, importTree.from(), exp));
            firstToken = context.get(from);
        }

//        while (firstToken.getType() == TokenType.K_CLASS) {
//
//        }
        return new MergeTree(selfNode, from);
    }


    public MergeTree pImportDeclaration(List<Token> context, int from) {
        String nodeSign = Thread.currentThread().getStackTrace()[1].getMethodName().substring(1);
        Node<Token> selfNode = Helper.buildNode(TokenType.IMPORT_DECLARATION, nodeSign);
        Node<Token> packageSpecifierNode = Helper.buildNode(TokenType.PACKAGE_SPECIFIER, TokenType.PACKAGE_SPECIFIER.toString());
        selfNode.addChild(packageSpecifierNode);
        TokenType[] exp = new TokenType[]{TokenType.NEWLINE, TokenType.F_SEMI, TokenType.F_DOT};
        from++;
        expect(nodeSign, context, from, TokenType.ID);
        while (context.get(from).getType() == TokenType.ID) {
            packageSpecifierNode.setText(packageSpecifierNode.getText() + "." + context.get(from).getText());
//            selfNode.addChild(Helper.buildNode(context.get(from)));
            expect(nodeSign, context, from + 1, exp);
            if (isExpected(context, from + 1, TokenType.F_DOT)) {
                expect(nodeSign, context, from + 2, TokenType.ID);
                from += 2;
            } else {
                from += 2;
                break;
            }
        }
        return new MergeTree(selfNode, from);
    }
    public MergeTree pClassDeclaration(List<Token> context, int from) {
        String nodeSign = Thread.currentThread().getStackTrace()[1].getMethodName().substring(1);
        expect(nodeSign, context, from, new TokenType[]{TokenType.K_CLASS});


        return null;
    }

    /** support */
    public void terminate(String syntaxSign, List<TokenType> expected, Token unexpected) {
        StringBuilder error = new StringBuilder();
        if (unexpected == null) {
            error.append("Unfinished ").append(syntaxSign);
        } else {
            String tokenText = unexpected.getType() == TokenType.NEWLINE ? "[NEWLINE]" : unexpected.getText();
            error.append(syntaxSign).append(" unexpected token ")
                    .append("\033[31;1m").append(tokenText).append("\033[0m");
        }
        error.append(", expect (")
                .append(String.join(", ", expected.stream().map(TokenType::toString).toList()))
                .append(") tokens.");
        throw new RuntimeException(error.toString());
    }


    public boolean startWith(List<Token> context, int from, TokenType... tokenTypes) {
        return false;
    }

    public boolean isExpected(List<Token> context, int from, TokenType expected) {
        return Objects.equals(expected, context.get(from).getType());
    }

    public boolean isExpected(List<Token> context, int from, TokenType[] expected) {
        return List.of(expected).contains(context.get(from).getType());
    }

    public void expect(String syntaxSign, List<Token> context, int from, TokenType expected) {
        expect(syntaxSign, context, from, List.of(expected));
    }

    public void expect(String syntaxSign, List<Token> context, int from, TokenType[] expected) {
        expect(syntaxSign, context, from, List.of(expected));
    }

    public void expect(String syntaxSign, List<Token> context, int from, List<TokenType> expected) {
        if (from >= context.size()) {
            terminate(syntaxSign, expected, null);
        }
        if (!expected.contains(context.get(from).getType())) {
            terminate(syntaxSign, expected, context.get(from));
        }
    }

    public interface Callback {
        void apply();
    }

    public int skipEndMark(List<Token> context, int from) {
        return skipEndMark(context, from, null);
    }

    public int skipEndMark(List<Token> context, int from, Callback callback) {
        int iter = from;
        while (context.get(iter).getType() == TokenType.NEWLINE
                || context.get(iter).getType() == TokenType.F_SEMI) {
            iter++;
            if (iter >= context.size()) {
                if (callback != null) {
                    callback.apply();
                }
                break;
            }
        }
        return iter;
    }
}
