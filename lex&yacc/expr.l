%{
#include <stdio.h>
#include "expr.yacc.h"
extern YYSTYPE yylval;
%}
QUO_LEFT    \(
QUO_RIGHT   \)
KEY_AND     and|AND
KEY_OR      or|OR
KEY_LINK    {KEY_AND}|{KEY_OR}
KEY_TRUE    true|True|TRUE
KEY_FALSE   false|False|FALSE
KEY_BOOL    {KEY_TRUE}|{KEY_FALSE}
KEY_IN      in|IN
KEY_BETWEEN between|BETWEEN
KEY_LIKE    like|LIKE
VAL_STR     \"(.)*\"|'(.)*'
VAL_NUM        (-)?([0-9]+.)?[0-9]+
VALUE       {VAL_STR}|{VAL_NUM}|{KEY_BOOL}
FIELD       _?[a-zA-Z_][a-zA-Z0-9_]*
OP_EQ       =
OP_NEQ      !=
OP_GT       >
OP_GTE      >=
OP_LT       <
OP_LTE      <=
OPERATOR    {OP_EQ}|{OP_NEQ}|{OP_GT}|{OP_GTE}|{OP_LT}|{OP_LTE}|{KEY_IN}|{KEY_BETWEEN}|{KEY_LIKE}
%%
[[:space:]]+    {/*printf("[LEX] space\n");*/}
{QUO_LEFT}      {/*printf("[LEX] quote : %s\n", yytext);*/printf("[LEX] TK_QUOTE_LEFT\n");yylval.gName=yytext;return TK_QUOTE_LEFT;}
{QUO_RIGHT}     {/*printf("[LEX] quote : %s\n", yytext);*/printf("[LEX] TK_QUOTE_RIGHT\n");yylval.gName=yytext;return TK_QUOTE_RIGHT;}
{KEY_LINK}      {/*printf("[LEX] link : %s\n", yytext);*/printf("[LEX] TK_LINK\n");yylval.gName=yytext;return TK_LINK;}
{FIELD}         {/*printf("[LEX] field : %s\n", yytext);*/printf("[LEX] TK_FIELD\n");yylval.gName=yytext;return TK_FIELD;}
{OPERATOR}      {/*printf("[LEX] operator : %s\n", yytext);*/printf("[LEX] TK_OPERATOR\n");yylval.gName=yytext;return TK_OPERATOR;}
{VALUE}         {/*printf("[LEX] value : %s\n", yytext);*/printf("[LEX] TK_VALUE\n");yylval.gName=yytext;return TK_VALUE;}
%%
int yywrap()
{
    return 1;
}