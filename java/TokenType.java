package com.jpro;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    V_STR("v_str"),
    V_CHAR("v_char"),
    V_INT("v_int"),
    V_FLOAT("v_float"),
    K_BYTE("byte"),
    K_STR("string"),
    K_CHAR("char"),
    K_INT("int"),
    K_FLOAT("float"),
    K_BOOL("bool"),
    K_DEF("def"),
    K_VAR("var"),
    K_VAL("val"),
    K_CONST("const"),
    K_RETURN("return"),
    K_VOID("void"),
    K_PUB("pub"),
    K_IMPORT("import"),
    K_CLASS("class"),
    K_TRUE("true"),
    K_FALSE("false"),
    K_IF("if"),
    K_ELSE("else"),
    K_MATCH("match"),
    K_CASE("case"),
    K_FOR("for"),
    K_CONTINUE("continue"),
    K_BREAK("break"),
    K_WHILE("while"),
    F_ASSIGN("assign"),
    F_SEMI("semi"),
    F_COMMA("comma"),
    F_DOT("dot"),
    F_COLON("colon"),
    F_ARROW("arrow"),
    WHITESPACE(""),
    NEWLINE(""),
    F_ADD("add"),
    F_INC("inc"),
    F_DEC("dec"),
    F_SUB("sub"),
    F_MUL("mul"),
    F_DIV("div"),
    F_MOD("mod"),
    B_INV("inv"),
    B_AND("bit_and"),
    B_OR("bit_or"),
    F_EQ("eq"),
    F_NE("ne"),
    F_GT("gt"),
    F_LT("lt"),
    F_GE("ge"),
    F_LE("le"),
    F_NOT("not"),
    F_AND("and"),
    F_OR("or"),
    F_WHY("why"),
    L_BRACE("left_brace"),
    R_BRACE("right_brace"),
    L_PAREN("left_paren"),
    R_PAREN("right_paren"),
    L_SQUARE("left_square"),
    R_SQUARE("right_square"),
    ID("id"),
    COMMENT("comment");

    private final String sign;

    TokenType(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return sign.toUpperCase();
    }
}
