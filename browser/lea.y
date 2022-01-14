start
	: modulesDeclare
	| modulesDeclare classDefine
	| classDefine
	;

// modulesDeclare classDefine | classDefine;

x1: x2;

x2: x1;

header: [p0, p1, ...];

Set<string>

optionalModulesDeclare: modulesDeclare | optionalClassDefine;

optionalClassDefine: classDefine | EOF;

xxx: xxx atom | atom;

modulesDeclare: modulesDeclare moduleAtomDeclare | moduleAtomDeclare;

moduleAtomDeclare: IMPORT module SEMI;

classDefine: CLASS LB RP;


