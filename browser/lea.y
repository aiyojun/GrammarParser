root
    : start
    ;

start
	: moduleDeclare
	| moduleDeclare classesDefine
	| classesDefine
	;

moduleDeclare
    : moduleDeclare IMPORT IDENTIFIER SEMI
    | IMPORT IDENTIFIER SEMI
    ;

classesDefine
    : classesDefine classDefine
    | classDefine
    ;

classDefine
    : CLASS IDENTIFIER LB RB
    | CLASS IDENTIFIER LB classMemberDefine RB
    ;

classMemberDefine
    : classMemberDefine memberDefine
    | memberDefine
    ;

memberDefine
    : limitedMemberDefine
    | PRIVATE limitedMemberDefine
    ;

limitedMemberDefine
    : methodDefine
    | variableDefine
    ;

methodDefine
    : DEF IDENTIFIER LP RP LB RB
    ;

variableDefine
    : VAL IDENTIFIER COLON IDENTIFIER SEMI
    | VAR IDENTIFIER COLON IDENTIFIER SEMI
    ;
