package com.jpro;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Helper {
    public static Node<Token> buildNode(TokenType tokenType, String text) {
        return Node.build(Token.build(tokenType, text));
    }

    public static Node<Token> buildNode(Token token) {
        return Node.build(token);
    }

    public static String jsonTree(Node<Token> node, int depth) {
        if (!node.hasChild()) return "{\"name\": \"" + node.getValue().getText() + "\",\"children\":[]}";
        return "{\"name\": \"" + node.getValue().getText() + "\",\"children\":[" +
                String.join(",",
                        node.getChildren().stream().map(child -> jsonTree(child, depth + 1))
                                .toList()) + "]}";
    }

    public static Node<Token> adjust(Node<Token> node) {
        while (node.getValue().getText().startsWith("compose") && node.getChildrenCount() == 1) {
            node = node.getChild(0);
        }
        if (node.hasChild()) {
            for (int i = 0; i < node.getChildrenCount(); i++) {
                node.replaceChild(i, adjust(node.getChild(i)));
            }
        }
        return node;
    }

    public static String readFile(String path) {
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
}
