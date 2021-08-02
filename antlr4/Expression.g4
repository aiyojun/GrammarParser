grammar Expression;

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

prog: expr OVER;

expr: TK_QUOTE_LEFT expr TK_QUOTE_RIGHT
    | expr TK_LINK expr
    | exprItem;

exprItem: atomName atomOp atomValue
      ;
atomName:  TK_FIELD;
atomOp:    TK_OPERATOR;
atomValue: TK_VALUE;
