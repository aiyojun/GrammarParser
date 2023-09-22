grammar LangLea;

WS: [ \t] -> skip;
ID: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*;
NUMBER: ('0'..'9')+('.' ('0'..'9')+)?;
HEX: '0' ('x' | 'X') ('0'..'9' | 'a'..'f' | 'A'..'F')+;
OCT: '0' ('o' | 'O') ('0'..'7')+;
BIN: '0' ('b' | 'B') ('0'..'1')+;
CHAR: '\'' (('\u0020'..'\u0026' | '\u0028'..'\u007F') | ('\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\'))) '\'';
STRING: '"' (('\u0020'..'\u0021' | '\u0023'..'\u007F') | ('\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\')))* '"';





start: stmts EOF;
stmts: stmts '\n'+ stmt | stmt;
stmt:
      'decl' clsdecl
    | 'decl' fndecl
    | 'decl' vardecl
    | fndef
    ;
clsdecl: 'class' ID '{' '\n'* membersdecl? '}';
membersdecl: membersdecl membdecl | membdecl;
membdecl: 'private'? (fndecl | vardecl) ('\n' | ';')+;
fndecl: 'def' ID '(' (argsdecl | ) ')' ':' typedecl;
vardecl: ('val' | 'var') ID ':' typedecl;

typedecl: complextype;

lambdatype: '(' typelist ')' '->' complextype;
complextype: basictype | ID | lambdatype;

typelist: typelist ',' complextype | complextype | ;

basictype: 'i8' | 'i32' | 'i64' | 'f32' | 'f64' | 'string' | 'char' | 'bool' | 'void';
argsdecl: argsdecl argdecl | argdecl;
argdecl: ID ':' typedecl;
fndef: fndecl '{' impls '}';

impls: impls impl | impl | ;
impl: vardef | assignstmt | ifstmt | whilestmt | matchstmt | returnstmt | continuestmt | breakstmt | ('\n' | ';')+;

vardef: ('val' | 'var') ID (':' typedecl)? '=' rightvalue;
assignstmt: (rightvalue '=')? rightvalue ('\n' | ';');



lambdaimpl: '(' lambdaargslist ')' '->' (rightvalue | '{' impls '}');
lambdaargslist: lambdaargslist ',' lambdaarg | lambdaarg | ;
lambdaarg: ID ':' complextype;

// '{' '\n'*
// '\n'* '}'
// '(' '\n'*
// '\n'* ')'
// '[' '\n'*
// '\n'* ']'
ifstmt: ifstmt 'else' onlyifstmt | onlyifstmt;
onlyifstmt: 'if' '(' rightvalue ')' '{' impls '}';
forstmt: 'for' '('  ')';
whilestmt: 'while' '(' rightvalue ')' '{' impls '}';
matchstmt: 'match' '(' rightvalue ')' '{' '\n'* multicasestmt '}';
multicasestmt: multicasestmt casestmt | casestmt;
casestmt: rightvalue '->' ('{' impls rightvalue '}' | rightvalue) ('\n' | ';')+;
returnstmt: 'return' rightvalue;
continuestmt: 'continue';
breakstmt: 'break';

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






