skip: ^[ ]
FIELD: ^[a-zA-Z_][a-zA-Z0-9_]*
SHOW: ^(show|SHOW)
WHEN: ^(when|WHEN)
AND: ^(and|AND)
OR: ^(or|OR)
LP: ^\(
RP: ^\)
OP: ^(=|!=|>=|>|<=|<)
VALUE: ^(((-)?([0-9]+\.)?[0-9]+)|('((\\')|[^'])*')|("((\\")|[^"])*"))