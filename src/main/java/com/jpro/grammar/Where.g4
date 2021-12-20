grammar Where;

WS:             [ \t] -> skip;
TK_QUOTE_LEFT:  QUO_LEFT;
TK_QUOTE_RIGHT: QUO_RIGHT;
TK_OPERATOR:    OPERATOR;
TK_VALUE:       VALUE;
TK_AND:         (' ')+ KEY_AND;
TK_OR:          (' ')+ KEY_OR;

COMMA:      ',';
OVER:       ';';
QUO_LEFT:   '(';
QUO_RIGHT:  ')';
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
VAL_NUM:    [-]?([0-9]+'.')?[0-9]+;
FIELD:      ('a'..'z'|'A'..'Z'|'\u4E00'..'\u9FA5')+('a'..'z'|'A'..'Z'|'\u4E00'..'\u9FA5'|'_')*;
OP_EQ:      '=';
OP_NEQ:     '!=';
OP_GT:      [>];
OP_GTE:     '>=';
OP_LT:      [<];
OP_LTE:     '<=';
OPERATOR:   OP_EQ|OP_NEQ|OP_GT|OP_GTE|OP_LT|OP_LTE|KEY_LIKE;

prog: expr OVER;


expr: expr TK_OR unionItem
    | unionItem
;

unionItem: unionItem TK_AND exprItem
         | exprItem
;

exprWithQuote: TK_QUOTE_LEFT expr TK_QUOTE_RIGHT
;

exprItem: atomName atomOp | exprWithQuote
;

atomName:  FIELD
;

atomOp: TK_OPERATOR atomValue
      | KEY_BETWEEN atomValue TK_AND atomValue
      | KEY_IN TK_QUOTE_LEFT atomList TK_QUOTE_RIGHT
;

atomList: atomList COMMA atomValue
        | atomValue
;

atomValue: TK_VALUE
;
