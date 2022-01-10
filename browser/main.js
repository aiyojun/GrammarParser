import { Lexer, ParserTree, TreeNode, Grammar } from 'GrammarParser';

function main() {
    console.info(">> parser running...");
    let str     = "show xxx when (a  =   12)";
    let words   = "skip: ^[ ]\nFIELD: ^[a-zA-Z_][a-zA-Z0-9_]*\nSHOW: ^(show|SHOW)\nWHEN: ^(when|WHEN)\nAND: ^and\nLP: ^\\(\nRP: ^\\)\nOP: ^(=|!=|>|>=|<|<=)\nVALUE: ^[0-9]+";
    let grammar = "start: SHOW FIELD WHEN expr;\nexpr: expr AND atom | atom;\natom: LP expr RP | FIELD OP VALUE";
    let lexer   = Grammar.lexer(words);
    let tree    = Grammar.parser(grammar);
    tree.parse(str, "start", lexer);
    console.info(">> isOver: " + tree.isOver());
    console.info(">> build tree ...");
    console.info(">> print tree :");
    tree.printTree(tree.getTree());
}

main();