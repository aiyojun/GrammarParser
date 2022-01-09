class LexerError extends Error {
    constructor(message?: string) {
        super(message);
    }
}

class GrammarError extends Error {
    constructor(message?: string) {
        super(message);
    }
}

class Token {
    text: string;
    type: string;
    constructor(s: string, t: string) {
        this.text = t;
        this.type = s;
    }
}

enum RuleType { REG, KEYWORD }

class Rule {
    uid: number
    suid: string
    type: RuleType
    constructor(u: number, s: string, rt: RuleType) {
        this.uid = u
        this.suid = s
        this.type = rt
    }
}

class RegRule extends Rule {
    reg: RegExp
    constructor(u: number, s: string, r: RegExp) {
        super(u, s, RuleType.REG)
        this.uid = u
        this.reg = r
    }
}

class Lexer {
    private _stringStream: string
    private _rules: Array<RegRule> = []
    set(s: string) {
        this._stringStream = s
    }
    rule(tokenName: string, regex: RegExp) {
        this._rules.push(new RegRule(this._rules.length, tokenName, regex));
    }
    lex(): Token {
        if (this._stringStream.length === 0)
            return new Token("EOF", "");
        let tokens: Array<Token> = []
        for (let i: number = 0; i < this._rules.length; i++) {
            let r: Array<string> = this._stringStream.match(this._rules[i].reg)
            if (r !== null) {
                tokens.push(new Token(this._rules[i].suid, r[0]))
            }
        }
        if (tokens.length == 1) {
            this._stringStream = this._stringStream.substr(tokens[0].text.length)
            return tokens[0]
        }
        if (tokens.length > 1) {
            for (let i = 1; i < tokens.length; i++) {
                if (tokens[i].text === tokens[0].text) {
                    this._stringStream = this._stringStream.substr(tokens[i].text.length)
                    return tokens[i]
                }
            }
            this._stringStream = this._stringStream.substr(tokens[0].text.length)
            return tokens[0]
        }
        throw new LexerError("Unexpected token: \""
            + this._stringStream.substr(0, 10) + "\"")
    }
}

class TreeNode {
    /** core data structure */
    private _uid: number = 0;
    private _parent: TreeNode | null = null;
    private _child: Array<TreeNode> = [];
    /** parsed data */
    private _text: string = "";
    private _type: string = "";
    private _template: Array<string> = [];
    /** helper */
    // @ts-ignore
    private _options: Map<string, Array<string>> | null = null;
    // @ts-ignore
    private _optionConcat: Map<string, Array<string>> | null = null;
    private _isRecursion: boolean = false;

    setUid(u: number): TreeNode {this._uid = u;return this;}
    setParent(p: TreeNode): TreeNode {this._parent = p; return this;}
    addChild(c: TreeNode): TreeNode {this._child.push(c); return this;}
    replaceChild(i: number, c: TreeNode): TreeNode {this._child[i] = c; return this;}
    setText(t: string): TreeNode {this._text = t; return this;}
    setType(t: string): TreeNode {this._type = t; return this;}
    setTemplate(t: Array<string>): TreeNode {this._template = t; return this;}
    // @ts-ignore
    setOptions(o: Map<string, Array<string>>): TreeNode {this._options = o; return this;}
    // @ts-ignore
    setOptionConcat(c: Map<string, Array<string>>): TreeNode {this._optionConcat = c; return this;}
    setRecursion(r: boolean): TreeNode {this._isRecursion = r; return this;}

    getUid(): number {return this._uid;}
    getParent(): TreeNode | null {return this._parent;}
    getChildren(): Array<TreeNode> {return this._child;}
    getChild(i: number): TreeNode {return this._child[i];}
    getChildSize(): number {return this._child.length;}
    getText(): string {return this._text;}
    getType(): string {return this._type;}
    getTemplate(): Array<string> {return this._template;}
    // @ts-ignore
    getOptions(): Map<string, Array<string>> | null {return this._options;}
    // @ts-ignore
    getOptionConcat(): Map<string, Array<string>> | null {return this._optionConcat;}
    isRecursion(): boolean {return this._isRecursion;}
}

class GrammarItem {
    recursionItems: Array<string> = [];
    // @ts-ignore
    optionConcat: Map<string, Array<string>> = new Map<string, Array<string>>();
    isRecursion: boolean = false;
    // @ts-ignore
    options: Map<string, Array<string>> = new Map<string, Array<string>>();
    // @ts-ignore
    static parse(handle: Map<string, Array<string>>, recursion: Array<string> | null = null): GrammarItem {
        let r: GrammarItem = new GrammarItem();
        if (recursion === null) {
            r.options = handle;
        } else {
            r.isRecursion = true;
            r.optionConcat = handle;
            r.recursionItems = recursion;
        }
        return r;
    }
}

class TreeNodeFactory {
    private uuidGenerator: number = 0;
    // @ts-ignore
    private grammar: Map<string, GrammarItem> = new Map<string, GrammarItem>();
    // @ts-ignore
    private nonLeafSignature: Set<string> = new Set<string>();
    // @ts-ignore
    private recursionSignature: Set<string> = new Set<string>();

    printGrammar(): void {
        console.info(">> grammar print:");
        for (let key of this.grammar.keys()) {
            console.info(">>   " + key);
        }
    }

    // @ts-ignore
    rule(signature: string, handle: Map<string, Array<string>>, recursion: Array<string> | null = null): TreeNodeFactory {
        if (recursion !== null) {
            this.recursionSignature.add(signature);
        }
        this.nonLeafSignature.add(signature);
        this.grammar.set(signature, GrammarItem.parse(handle, recursion));
        return this;
    }

    isLeaf(type: string): boolean {
        return !this.nonLeafSignature.has(type);
    }

    build(type: string): TreeNode {
        let r: TreeNode = new TreeNode().setUid(++this.uuidGenerator).setType(type);
        if (!this.grammar.has(type)) {
            throw new GrammarError("unknown type " + type);
        }
        if (this.recursionSignature.has(type)) {
            r.setRecursion(true)
                .setTemplate(this.grammar.get(type).recursionItems)
                .setOptionConcat(this.grammar.get(type).optionConcat);
        } else {
            r.setOptions(this.grammar.get(type).options);
        }
        // switch (type) {
        //     case "start":
        //         // @ts-ignore
        //         r.setOptions(new Map<string, Array<string>>([
        //                 ["SHOW", ["SHOW", "FIELD", "WHEN", "expr"]]
        //             ]));
        //         break;
        //     case "expr":
        //         r.setRecursion(true)
        //             .setTemplate(["atom"])
        //             // @ts-ignore
        //             .setOptionConcat(new Map<string, Array<string>> ([
        //                 ["AND", ["AND", "atom"]]
        //             ]));
        //         break;
        //     case "atom":
        //         // @ts-ignore
        //         r.setOptions(new Map<string, Array<string>>([
        //             ["LP", ["LP", "expr", "RP"]],
        //             ["FIELD", ["FIELD", "OP", "VALUE"]]
        //         ]))
        //         break;
        //     default:
        //         throw new GrammarError("unknown type " + type)
        // }
        return r;
    }

    leaf(p: TreeNode, token: Token): TreeNode {
        return new TreeNode()
            .setUid(++this.uuidGenerator)
            .setParent(p)
            .setText(token.text)
            .setType(token.type)
            ;
    }
}

class ParserTree {
    /** from global */
    private root: TreeNode;
    private errorExpected: Array<string> = [];
    /** local record */
    private ptr: TreeNode;
    private factory: TreeNodeFactory = new TreeNodeFactory();

    // @ts-ignore
    rule(signature: string, handle: Map<string, Array<string>>, recursion: Array<string> | null = null): void {
        this.factory.rule(signature, handle, recursion);
    }

    parse(grammarStart: string, lexer: Lexer): void {
        this.root = this.factory.build(grammarStart);
        this.ptr = this.root;
        let token: Token;
        while ((token = lexer.lex()).type != "EOF") {
            this.buildTree(this.factory, this.ptr, token);
        }
    }

    parseDebug(grammarStart: string, tokens: Array<Token>) {
        this.factory.printGrammar();
        this.root = this.factory.build(grammarStart);
        this.ptr = this.root;
        tokens.forEach(token => this.buildTree(this.factory, this.ptr, token));
    }

    getTree(): TreeNode {
        return this.root;
    }

    printTree(node: TreeNode) {
        if (node.getParent() !== null) {
            console.info(node.getText()
                + "[" + node.getUid() + ":" + node.getParent().getUid()
                + "#" + node.getType() +"]");
        } else {
            console.info(node.getText() + "[" + node.getUid()+ "#" + node.getType() +"]");
        }
        if (node.getChildSize() > 0) {
            node.getChildren().forEach(chi => this.printTree(chi));
        }
    }

    private buildTree(factory: TreeNodeFactory, treeNode: TreeNode, token: Token): void {
        if (treeNode.getChildSize() === 0) {
            if (treeNode.getTemplate() === null || treeNode.getTemplate().length === 0) {
                if (!treeNode.getOptions().has(token.type)) {
                    let exp: Array<string> = []
                    for (let e in treeNode.getOptions().keys()) {
                        exp.push(e);
                    }
                    throw new GrammarError("Unexpected: " + token.text + "(" + token.type +
                        "); expect: " + this.errorExpected.concat(exp).join(","));
                }
                treeNode.setTemplate(treeNode.getOptions().get(token.type));
            }
        } else if (treeNode.getChildSize() === treeNode.getTemplate().length) {
            if (!treeNode.isRecursion()) {
                this.ptr = treeNode.getParent();
                this.buildTree(factory, this.ptr, token);
                return;
            }
            if (treeNode.getOptionConcat().has(token.type)) {
                let node = factory.build(treeNode.getType())
                    .setParent(this.ptr.getParent())
                    .addChild(this.ptr)
                    .setTemplate([treeNode.getType()].concat(treeNode.getOptionConcat().get(token.type)));
                this.ptr.getParent().replaceChild(this.ptr.getParent().getChildSize() - 1, node);
                this.ptr.setParent(node);
                this.ptr = node;
                this.buildTree(factory, this.ptr, token);
                return;
            } else {
                this.errorExpected = [];
                for (let link in treeNode.getOptionConcat().keys()) {
                    this.errorExpected.push(link);
                }
                this.ptr = this.ptr.getParent();
                this.buildTree(factory, this.ptr, token);
                return;
            }
        }
        let expected: string = treeNode.getTemplate()[treeNode.getChildSize()];
        if (factory.isLeaf(expected)) {
            if (token.type !== expected) {
                throw new GrammarError("Unexpected: " + token.text
                    + "("+ token.type + "); expect: " + expected);
            }
            treeNode.addChild(factory.leaf(treeNode, token));
        } else {
            this.ptr = factory.build(expected).setParent(treeNode);
            this.ptr.getParent().addChild(this.ptr);
            this.buildTree(factory, this.ptr, token);
        }
    }
}

class Grammar {
    parse(grammar: string): ParserTree {
        let parserTree: ParserTree = new ParserTree();
        let rules: Array<string> = grammar.replace("\n", "")
            .replace("\t", "")
            .split(";").filter(line => line != "");
        rules.forEach(line => {
            let split_0: Array<string> = line.replace("\n", "").split(":").filter(word => word != "")
            if (split_0.length != 2) {
                throw new GrammarError("grammar string error");
            }
            let signature = split_0[0].replace(" ", "");
            let items: Array<string> = split_0[1].split("|");
            // console.info("items: " + items);
            // @ts-ignore
            let m: Map<string, Array<string>> = new Map<string, Array<string>>();
            let isRecursion: boolean = false;
            let recursionItems: Array<string> | null = null;
                let count: number = 0;
            for (let index in items) {
                let s = items[index];
                // console.info("s: " + s);
                let words: Array<string> = s.split(" ").filter(word => word != "");
                if (words.length === 0) {
                    throw new GrammarError("grammar rule error");
                }
                if (words.indexOf(signature) != -1) {
                    isRecursion = true;
                    if (words[0] !== signature) {
                        throw new GrammarError("grammar recursion rule error: not left recursion!");
                    }
                    words = words.slice(1);
                    if (words.length === 0) {
                        throw new GrammarError("grammar recursion rule error: invalid self recursion!");
                    }
                    if (words.indexOf(signature) != -1) {
                        throw new GrammarError("grammar recursion rule error: only one recursion item allowed!");
                    }
                } else {
                    count++;
                    recursionItems = words;
                }
                m.set(words[0], words);
            }
                if (isRecursion && (count > 1 || count == 0)) {
                    throw new GrammarError("grammar recursion rule error: only one recursion item(must) allowed! number: " + count);
                }
                // console.info("-- m size: " + m.size);
            if (isRecursion) {
                m.delete(recursionItems[0]);
                let ss = "";
                for (let key of m.keys()) {
                    ss += "  " + key + " : " + m.get(key).join(",") + ";  ";
                }
                console.info(">> " + signature + "[R]: " + ss);
                parserTree.rule(signature, m, recursionItems);
            } else {
                let ss = "";
                for (let key of m.keys()) {
                    ss += "  " + key + " : " + m.get(key).join(",") + ";  ";
                }
                console.info(">> " + signature + ": " + ss);
                parserTree.rule(signature, m);
            }
        });
        return parserTree;
    }
}

function TK(t: string, tp: string): Token {
    return new Token(tp, t);
}

function main() {
    console.info(">> parser running...");
    let tokens: Array<Token> = [
        TK("show", "SHOW"),
        TK("field_a", "FIELD"),
        TK("when", "WHEN"),
        TK("a", "FIELD"),
        TK("=", "OP"),
        TK("12", "VALUE"),
        TK("and", "AND"),
        TK("(", "LP"),
        TK("b", "FIELD"),
        TK("!=", "OP"),
        TK("'abc'", "VALUE"),
        // TK("and", "AND"),
        // TK("c", "FIELD"),
        // TK("<", "OP"),
        // TK("90", "VALUE"),
        TK(")", "RP"),
    ];
    let grammar: string = "start: SHOW FIELD WHEN expr;\nexpr: expr AND atom | atom;\natom: LP expr RP | FIELD OP VALUE";
    let tree: ParserTree = new Grammar().parse(grammar)//new ParserTree();
    tree.parseDebug("start", tokens);
    console.info(">> build tree ...");
    console.info(">> print tree :");
    tree.printTree(tree.getTree());
}

export {Token, Lexer,
    LexerError, GrammarError,
    RuleType, Rule,
    TreeNode, TreeNodeFactory,
    ParserTree, RegRule,
    GrammarItem, Grammar};

main()