grammar lang_rightvalue;

WS: [ \t] -> skip;
ID: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*;
NUMBER: ('0'..'9')+('.' ('0'..'9')+)?;
HEX: '0' ('x' | 'X') ('0'..'9' | 'a'..'f' | 'A'..'F')+;
OCT: '0' ('o' | 'O') ('0'..'7')+;
BIN: '0' ('b' | 'B') ('0'..'1')+;
CHAR: '\'' (('\u0020'..'\u0026' | '\u0028'..'\u007F') | ('\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\'))) '\'';
STRING: '"' (('\u0020'..'\u0021' | '\u0023'..'\u007F') | ('\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\')))* '"';


start: rightvalue EOF;
rightvalue: ternaryexpr;
ternaryexpr:
    ternaryexpr '?' ternaryexpr ':' ternaryexpr
    | boolorexpr
    ;
boolorexpr: boolorexpr '||' boolandexpr | boolandexpr;
boolandexpr: boolandexpr '&&' bitorexpr | bitorexpr;
bitorexpr: bitorexpr '|' bitnotexpr | bitnotexpr;
bitnotexpr: '^' bitnotexpr | bitandexpr ;
bitandexpr: bitandexpr '&' boolequalexpr | boolequalexpr;
boolequalexpr: boolequalexpr ('==' | '!=') boolcompareexpr | boolcompareexpr;
boolcompareexpr: boolcompareexpr ('>=' | '<=' | '>' | '<') plusexpr | plusexpr;
plusexpr: plusexpr ('+' | '-') mulexpr | mulexpr;
mulexpr: mulexpr ('*' | '/' | '%') monocolarexpr | monocolarexpr;
monocolarexpr: '!' valexpr | '~' valexpr | valexpr;
valexpr:
      preexpr
    | CHAR
    | STRING
    | BIN
    | OCT
    | HEX
    | NUMBER
    | lambdaimpl
    ;
preexpr: ('++' | '--') loopaccessexpr | loopaccessexpr;
loopaccessexpr: loopaccessexpr accesstail | accesshead;
accesshead: '(' rightvalue ')' | ID;
accesstail: '(' argslist ')' | '[' rightvalue ']' | '.' ID | ('++' | '--');
argslist: argslist ',' rightvalue | rightvalue | ;



lambdaimpl: '(' lambdaargslist ')' '->' (rightvalue | '{'  '}');
lambdaargslist: lambdaargslist ',' lambdaarg | lambdaarg | ;
lambdaarg: ID ':' complextype;
complextype: basictype | ID | lambdatype;

basictype: 'i8' | 'i32' | 'i64' | 'f32' | 'f64' | 'string' | 'char' | 'bool' | 'void';
lambdatype: '(' typelist ')' '->' complextype;
typelist: typelist ',' complextype | complextype | ;
