package com.jpro;

import java.util.Arrays;
import java.util.List;

public class Parser {
    public record MergeTree(Node<Token> node, int from) {}

    interface Functor { MergeTree apply(List<Token> seq, int from); }
    /**
     * Normal calculator!
     */
    public static void main(String[] args) {
        List<Token> tokens =
                new Tokenizer().lex(Helper.readFile("/opt/jpro/Grammar/expr.lea")).stream()
                        .filter(p ->
                                p.value() != TokenType.COMMENT &&
                                        p.value() != TokenType.NEWLINE &&
                                        p.value() != TokenType.WHITESPACE
                        )
                        .map(p -> Pair.build(p.value() == TokenType.V_STR
                                ? p.key().substring(1, p.key().length() - 1) : p.key(), p.value()))
                        .map(p -> Token.build(p.value(), p.key())).toList();
        tokens.forEach(token -> System.out.printf("%s ", token.getType()));
        System.out.println();

        MergeTree mergeTree = SimpleExpressionParser.composeTop(tokens, 0);
        System.out.println(Helper.jsonTree(Helper.adjust(mergeTree.node), 0));
        System.out.println("stop at : " + mergeTree.from);
    }


    public static class SimpleExpressionParser {

        public static MergeTree composeTop(List<Token> seq, int from) {
            return composeOr(seq, from);
        }

        public static MergeTree generalCompose(List<TokenType> expected, Functor nextComposer, List<Token> seq, int from) {
            MergeTree beginner = nextComposer.apply(seq, from);
            boolean hasOp = beginner.from < seq.size() && expected.contains(seq.get(beginner.from).getType());
            Node<Token> self = Helper.buildNode(TokenType.OPERATOR, hasOp ? seq.get(beginner.from).getText() : "compose");
            self.addChild(beginner.node);
            while (beginner.from < seq.size() &&
                    expected.contains(seq.get(beginner.from).getType())) {
                beginner = nextComposer.apply(seq, beginner.from + 1);
                beginner.node.setParent(self);
                self.addChild(beginner.node);
            }
            return new MergeTree(self, beginner.from);
        }

        public static MergeTree composeOr(List<Token> seq, int from) {
            return generalCompose(List.of(TokenType.F_OR), SimpleExpressionParser::composeAnd, seq, from);
        }

        public static MergeTree composeAnd(List<Token> seq, int from) {
            return generalCompose(List.of(TokenType.F_AND), SimpleExpressionParser::composeBitOr, seq, from);
        }

        public static MergeTree composeBitOr(List<Token> seq, int from) {
            return generalCompose(List.of(TokenType.B_OR), SimpleExpressionParser::composeBitAnd, seq, from);
        }

        public static MergeTree composeBitAnd(List<Token> seq, int from) {
            return generalCompose(List.of(TokenType.B_AND), SimpleExpressionParser::composeEqNe, seq, from);
        }

        public static MergeTree composeEqNe(List<Token> seq, int from) {
            return generalCompose(Arrays.asList(TokenType.F_EQ, TokenType.F_NE),
                    SimpleExpressionParser::composeGteLte, seq, from);
        }

        public static MergeTree composeGteLte(List<Token> seq, int from) {
            return generalCompose(Arrays.asList(TokenType.F_GT, TokenType.F_GE, TokenType.F_LT, TokenType.F_LE),
                    SimpleExpressionParser::composeAddSubOperation, seq, from);
        }

        public static MergeTree composeAddSubOperation(List<Token> seq, int from) {
            return generalCompose(Arrays.asList(TokenType.F_ADD, TokenType.F_SUB),
                    SimpleExpressionParser::composeMulDivModOperation, seq, from);
        }

        public static MergeTree composeMulDivModOperation(List<Token> seq, int from) {
            return generalCompose(Arrays.asList(TokenType.F_MUL, TokenType.F_DIV, TokenType.F_MOD),
                    SimpleExpressionParser::composeAtom, seq, from);
        }

        public static MergeTree composeCall(List<Token> seq, int from) {
            Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "args");
            if (from + 1 < seq.size() && seq.get(from + 1).getType() == TokenType.R_PAREN)
                return new MergeTree(self, from + 2);
            MergeTree mergeTree = composeTop(seq, from + 1);
            {
                mergeTree.node.setParent(self);
                self.addChild(mergeTree.node);
            }
            while (mergeTree.from < seq.size() &&
                    seq.get(mergeTree.from).getType() == TokenType.F_COMMA) {
                mergeTree = composeTop(seq, mergeTree.from + 1);
                mergeTree.node.setParent(self);
                self.addChild(mergeTree.node);
            }
            if (seq.get(mergeTree.from).getType() != TokenType.R_PAREN)
                throw new RuntimeException("Unexpected token : " + seq.get(mergeTree.from).getText() + "; expect : )");
            return new MergeTree(self, mergeTree.from + 1);
        }

        public static MergeTree composeAccessArray(List<Token> seq, int from) {
//            Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "accessArray");
            MergeTree mergeTree = composeTop(seq, from + 1);
//            mergeTree.node.setParent(self);
//            self.addChild(mergeTree.node);
            if (seq.get(mergeTree.from).getType() != TokenType.R_SQUARE)
                throw new RuntimeException("Unexpected token : " + seq.get(mergeTree.from).getText() + "; expect : ]");
//            return new MergeTree(self, mergeTree.from + 1);
            return new MergeTree(mergeTree.node, mergeTree.from + 1);
        }

        public static MergeTree composeAccessMember(List<Token> seq, int from) {
//            Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "accessMember");
            if (seq.get(from + 1).getType() != TokenType.ID)
                throw new RuntimeException("Unexpected token : " + seq.get(from + 1).getText() + "; expect : member ID");
            Node<Token> member = Helper.buildNode(seq.get(from + 1));
//            member.setParent(self);
//            self.addChild(member);
            return new MergeTree(member, from + 2);
        }

        public static int composeTailIfNecessary(Node<Token> firstNode, List<Token> seq, int from) {
            List<TokenType> expected = Arrays.asList(TokenType.F_DOT, TokenType.L_PAREN, TokenType.L_SQUARE);
            if (!expected.contains(seq.get(from).getType())) return from;
            MergeTree mergeTree;
//            int counter = 0;
            do {
//                counter++;
//                if (counter > 10) {
//                    System.out.println("counter > 10!!!");
//                    System.exit(1);
//                }
                mergeTree = switch (seq.get(from).getType()) {
                    case F_DOT -> composeAccessMember(seq, from);
                    case L_PAREN -> composeCall(seq, from);
                    case L_SQUARE -> composeAccessArray(seq, from);
                    default -> throw new RuntimeException("Unexpected value: " + seq.get(from).getType());
                };
                Node<Token> mid = Helper.buildNode(TokenType.OPERATOR, switch (seq.get(from).getType()) {
                    case F_DOT -> ".";
                    case L_PAREN -> "call";
                    case L_SQUARE -> "[]";
                    default -> throw new RuntimeException("Unexpected value: " + seq.get(from).getType());
                });
                mid.addChild(mergeTree.node);
                mergeTree.node.setParent(mid);
                firstNode.addChild(mid);
                firstNode = mid;
//                mergeTree.node.setParent(firstNode);
//                firstNode.addChild(mergeTree.node);
//                firstNode = mergeTree.node;
                from = mergeTree.from;
            } while (expected.contains(seq.get(mergeTree.from).getType()));
            return mergeTree.from;
        }

        public static MergeTree composeAtom(List<Token> seq, int from) {
            List<TokenType> expected = Arrays.asList(
                    TokenType.V_INT, TokenType.V_FLOAT, TokenType.V_CHAR, TokenType.V_STR,
                    TokenType.K_TRUE, TokenType.K_FALSE, TokenType.ID, TokenType.L_PAREN);
            if (from >= seq.size())
                throw new RuntimeException("Unfinished expression, expect : "
                        + String.join(", ", expected.stream().map(TokenType::toString).toList()));
            if (!expected.contains(seq.get(from).getType()))
                throw new RuntimeException("Unexpected token : " + seq.get(from).getText()
                        + "; " + String.join(", ", expected.stream().map(TokenType::toString).toList()));
            List<TokenType> expectedTail = Arrays.asList(TokenType.F_DOT, TokenType.L_PAREN, TokenType.L_SQUARE);
            if (from + 1 < seq.size() && seq.get(from).getType() == TokenType.ID && expectedTail.contains(seq.get(from + 1).getType())) {
//                String op = switch (seq.get(from + 1).getType()) {
//                    case L_PAREN -> "call";
//                    case L_SQUARE -> "arrayAccess";
//                    case F_DOT -> "memberAccess";
//                    default -> throw new RuntimeException("");
//                };
//                Node<Token> self = Helper.buildNode(TokenType.OPERATOR, op);
//                Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "tail");
                Node<Token> func = Helper.buildNode(seq.get(from));
//                func.setParent(self);
//                self.addChild(func);
//                return new MergeTree(self, composeTailIfNecessary(self, seq, from + 1));
                return new MergeTree(func, composeTailIfNecessary(func, seq, from + 1));
            }
//            if (from + 1 < seq.size() && seq.get(from).getType() == TokenType.ID && seq.get(from + 1).getType() == TokenType.L_PAREN) {
//                Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "call");
//                Node<Token> func = Helper.buildNode(seq.get(from));
//                func.setParent(self);
//                self.addChild(func);
//                return new MergeTree(self, composeTailIfNecessary(self, seq, from + 1));
//            }
//            if (from + 1 < seq.size() && seq.get(from).getType() == TokenType.ID && seq.get(from + 1).getType() == TokenType.L_SQUARE) {
//                Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "arrayAccess");
//                Node<Token> func = Helper.buildNode(seq.get(from));
//                func.setParent(self);
//                self.addChild(func);
//                return new MergeTree(self, composeTailIfNecessary(self, seq, from + 1));
//            }
//            if (from + 1 < seq.size() && seq.get(from).getType() == TokenType.ID && seq.get(from + 1).getType() == TokenType.F_DOT) {
//                Node<Token> self = Helper.buildNode(TokenType.OPERATOR, "memberAccess");
//                Node<Token> func = Helper.buildNode(seq.get(from));
//                func.setParent(self);
//                self.addChild(func);
//                MergeTree callTree = composeAccessMember(seq, from + 1);
//                self.addChild(callTree.node);
//                callTree.node.setParent(self);
//                return new MergeTree(self, callTree.from);
//            }
            if (seq.get(from).getType() != TokenType.L_PAREN)
                return new MergeTree(Helper.buildNode(seq.get(from)), from + 1);
            Node<Token> parenNode = Helper.buildNode(TokenType.PAREN, "()");
            MergeTree mergeTree = composeTop(seq, from + 1);
            if (seq.get(mergeTree.from).getType() != TokenType.R_PAREN)
                throw new RuntimeException("Unexpected token : " + seq.get(mergeTree.from).getText() + "; expect : )");
            parenNode.addChild(mergeTree.node);
            return new MergeTree(parenNode, mergeTree.from + 1);
        }
    }
}
