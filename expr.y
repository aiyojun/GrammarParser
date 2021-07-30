%{
#include <stdio.h>
extern int yylex(void);
extern int yyparse(void);
extern FILE *yyin;

void yyerror(const char *s)
{
	printf("[YACC] [BISON] error %s\n", s);
}

int main()
{
	yyparse();
}
%}

%union {
	char *gName;
};

%token TK_QUOTE_LEFT TK_QUOTE_RIGHT TK_LINK
%token <gName> TK_FIELD
%token <gName> TK_OPERATOR
%token <gName> TK_VALUE

%%

expr: expr_0 TK_LINK expr {printf("[YACC] x-exp\n");}
    | TK_QUOTE_LEFT expr_0 TK_LINK expr TK_QUOTE_RIGHT {printf("[YACC] (x-exp)\n");}
    | expr TK_LINK expr_0 {printf("[YACC] exp-x\n");}
    | TK_QUOTE_LEFT expr TK_LINK expr_0 TK_QUOTE_RIGHT {printf("[YACC] (exp-x)\n");}
    | TK_QUOTE_LEFT expr TK_LINK expr TK_QUOTE_RIGHT {printf("[YACC] (exp-exp)\n");}
    | expr_0 {printf("[YACC] -2\n");}
    | expr TK_LINK expr_0 {printf("[YACC] exp-x\n");}
    | expr TK_LINK expr {printf("[YACC] exp-exp\n");}
    ;
expr_0: atom_0 atom_1 atom_2
      ;
atom_0: TK_FIELD    {printf("[YACC] key(%s)\n", $1);}
atom_1: TK_OPERATOR {printf("[YACC] op(%s)\n", $1);}
atom_2: TK_VALUE    {printf("[YACC] value(%s)\n", $1);}

%%

