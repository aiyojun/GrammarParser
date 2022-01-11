start: SHOW FIELD WHEN expr;
expr: expr OR andExpr | andExpr;
andExpr: andExpr AND atom | atom;
atom: LP expr RP | FIELD OP VALUE;