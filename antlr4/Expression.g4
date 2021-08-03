grammar Expression;

@parser::header {
import com.jqs.json.Json;
import com.jqs.frame.Atom;
}

@parser::members {
public static Atom piece;
public static String key;
public static String op;
public static String value;
public static List<Object> stack = new ArrayList<>(128);
}

WS:             [ \t] -> skip;
TK_QUOTE_LEFT:  QUO_LEFT;
TK_QUOTE_RIGHT: QUO_RIGHT;
TK_LINK:        (' ')+ KEY_LINK;
TK_FIELD:       FIELD;
TK_OPERATOR:    OPERATOR;
TK_VALUE:       VALUE;

OVER:       ';';
QUO_LEFT:   '(';
QUO_RIGHT:  ')';
KEY_LINK:   KEY_AND|KEY_OR;
KEY_OR:     'or'|'OR';
KEY_AND:    'and'|'AND';
VALUE:      VAL_STR|VAL_NUM|KEY_BOOL;
KEY_BOOL:   KEY_TRUE|KEY_FALSE;
KEY_TRUE:   'true'|'True'|'TRUE';
KEY_FALSE:  'false'|'False'|'FALSE';
KEY_IN:     'in'|'IN';
KEY_BETWEEN:'between'|'BETWEEN';
KEY_LIKE:   'like'|'LIKE';
VAL_STR:    '"'(~'"'|'\\''"')*'"'|'\''(~'\''|'\\''\'')*'\'';
VAL_NUM:    [-]?([0-9]+.)?[0-9]+;
FIELD:      ('a'..'z'|'A'..'Z'|'\u4E00'..'\u9FA5')+('a'..'z'|'A'..'Z'|'\u4E00'..'\u9FA5'|'_')*;
OP_EQ:      '=';
OP_NEQ:     '!=';
OP_GT:      [>];
OP_GTE:     '>=';
OP_LT:      [<];
OP_LTE:     '<=';
OPERATOR:   OP_EQ|OP_NEQ|OP_GT|OP_GTE|OP_LT|OP_LTE|KEY_IN|KEY_BETWEEN|KEY_LIKE;

prog: expr {stack.add(Expressions.getStageResult(stack));} OVER;

expr: exprWithQuote
    | expr TK_LINK {stack.add($TK_LINK.text.toLowerCase());} expr
    | exprItem
;

exprWithQuote: TK_QUOTE_LEFT {stack.add($TK_QUOTE_LEFT.text);} expr TK_QUOTE_RIGHT {stack.add(Expressions.getStageResult(stack));}
;

exprItem: atomName atomOp atomValue
;
atomName:  TK_FIELD {key = $TK_FIELD.text;}
;
atomOp:    TK_OPERATOR {op = $TK_OPERATOR.text.toLowerCase();}
;
atomValue: TK_VALUE {value = $TK_VALUE.text; stack.add(Expressions.getResult(key, op, value));}
;
