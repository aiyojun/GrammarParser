let words   = "skip: ^[ \\n]\n" +
    "IDENTIFIER: ^[a-zA-Z_][a-zA-Z0-9_]*\n" +
    "NUMBER: ^(((-)?([0-9]+\\.)?[0-9]+)|('((\\\\')|[^'])*')|(\"((\\\\\")|[^\"])*\"))\n" +
    "IMPORT: ^(import)\n" +
    "DEF: ^(def)\n" +
    "CLASS: ^(class)\n" +
    "PRIVATE: ^(private)\n" +
    "VAL: ^(val)\n" +
    "VAR: ^(var)\n" +
    "OR: ^(\\|\\|)\n" +
    "AND: ^(&&)\n" +
    "EQ: ^(=)\n" +
    "COMMA: ^(,)\n" +
    "SEMI: ^;\n" +
    "COLON: ^:\n" +
    "LB: ^\\{\n" +
    "RB: ^\\}\n" +
    "LP: ^\\(\n" +
    "RP: ^\\)\n" +
    "LSP: ^\\[\n" +
    "RSP: ^\\]\n" +
    "QM: ^\\?\n" +
    "DOT: ^\\.\n" +
    "NOT: ^\\!\n" +
    "PLUS: ^\\+\n" +
    "SUB: ^\\-\n" +
    "MUL: ^\\*\n" +
    "DIV: ^\\/\n" +
    "MOD: ^\\%\n" +
    "";
let grammar = "root\n" +
    "    : start\n" +
    "    ;\n" +
    "\n" +
    "start\n" +
    "\t: moduleDeclare\n" +
    "\t| moduleDeclare classesDefine\n" +
    "\t| classesDefine\n" +
    "\t;\n" +
    "\n" +
    "moduleDeclare\n" +
    "    : moduleDeclare IMPORT IDENTIFIER SEMI\n" +
    "    | IMPORT IDENTIFIER SEMI\n" +
    "    ;\n" +
    "\n" +
    "classesDefine\n" +
    "    : classesDefine classDefine\n" +
    "    | classDefine\n" +
    "    ;\n" +
    "\n" +
    "classDefine\n" +
    "    : CLASS IDENTIFIER LB RB\n" +
    "    | CLASS IDENTIFIER LB classMemberDefine RB\n" +
    "    ;\n" +
    "\n" +
    "classMemberDefine\n" +
    "    : classMemberDefine memberDefine\n" +
    "    | memberDefine\n" +
    "    ;\n" +
    "\n" +
    "memberDefine\n" +
    "    : limitedMemberDefine\n" +
    "    | PRIVATE limitedMemberDefine\n" +
    "    ;\n" +
    "\n" +
    "limitedMemberDefine\n" +
    "    : methodDefine\n" +
    "    | variableDefine\n" +
    "    ;\n" +
    "\n" +
    "methodDefine\n" +
    "    : DEF IDENTIFIER LP RP LB RB\n" +
    "    ;\n" +
    "\n" +
    "variableDefine\n" +
    "    : VAL IDENTIFIER COLON IDENTIFIER SEMI\n" +
    "    | VAR IDENTIFIER COLON IDENTIFIER SEMI\n" +
    "    ;\n";
let str     = "import os;\n" +
    "import sys;\n" +
    "\n" +
    "class Main {\n" +
    "    val uid: int;\n" +
    "\n" +
    "    private val type: string;\n" +
    "\n" +
    "    def main() {\n" +
    "        var x: int = 12;\n" +
    "        var s: string = \"text\";\n" +
    "        s = \"another text\";\n" +
    "        s = \"text_\" + x;\n" +
    "        s = getName();\n" +
    "        s = makeName(x);\n" +
    "        Main.method01();\n" +
    "        1 + 32;\n" +
    "        add();\n" +
    "    }\n" +
    "}";