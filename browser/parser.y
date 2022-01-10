start: SHOW FIELD WHEN expr;

expr: expr AND atom | atom;

atom: LP expr RP | FIELD OP VALUE