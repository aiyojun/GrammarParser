/*
 * Copyright (c) 2022, aiyojun. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES.
 *
 * This code is free software; At current stage, you
 * can use it freely, without any risks.
 */

/**
 * GrammarParser is a grammar parsing tool writen by
 * typescript(This file). You can define lexer and
 * parser by yourself, and the way to create grammar
 * rules is very easy and convinient.
 *
 * @author: aiyojun
 * If you have any question about this file, you can
 * send mail to me. My email: 1608450902@qq.com
 *
 * This file mainly include four parts:
 *     1. lexer       2. parser
 *     3. tree node   4. grammar parser
 */
////////////////////////////////////////////////////
// ***** 1. Exception definition *******************
////////////////////////////////////////////////////

/**
 * The {@code LexerError} will be thrown in token
 * lexing stage, if any problem occur.
 */
class LexerError extends Error {
    constructor(message?: string) {
        super(message);
    }
}
/**
 * The {@code GrammarError} will be thrown in AST
 * tree building stage, if any problem occur.
 */
class GrammarError extends Error {
    constructor(message?: string) {
        super(message);
    }
}
////////////////////////////////////////////////////
// ***** 2. Lexer and related tools definition *****
////////////////////////////////////////////////////
/**
 * The {@code Token} is output after passing lexer.
 * And now, i use the common method(type: string)
 * to represent inner grammar signature. Maybe enum
 * is better.
 */
class Token {
    text: string;
    type: string;
    /**
     * @param s raw token string
     * @param t token type, defined by yourself.
     *          like: FIELD, KEYWORD
     */
    constructor(s: string, t: string) {
        this.text = t;
        this.type = s;
    }
}

/**
 * Lexer rule definition. Using regex to create
 * Lexer rules!
 */
class RegRule {
    reg: RegExp;
    uid: number;
    suid: string;
    constructor(u: number, s: string, r: RegExp) {
        this.uid = u;
        this.suid = s;
        this.uid = u;
        this.reg = r;
    }
}
/**
 * The {@code Lexer} is a important part of
 * GrammarParser.
 */
class Lexer {
    private _stringStream: string
    private _rules: Array<RegRule> = []
    set(s: string) {
        this._stringStream = s
    }
    rule(tokenName: string, regex: RegExp): Lexer {
        this._rules.push(new RegRule(this._rules.length, tokenName, regex));
        return this;
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
        for (let index in tokens) {
            if (tokens[index].type === "skip") {
                this._stringStream = this._stringStream.substr(tokens[index].text.length)
                return this.lex();
            }
        }
        if (tokens.length == 1) {
            this._stringStream = this._stringStream.substr(tokens[0].text.length)
            return tokens[0]
        }
        if (tokens.length > 1) {
            let longest = {
                idx: 0,
                len: tokens[0].text.length
            };
            // @ts-ignore
            let same: Map<string, Array<number>> = new Map<string, Array<number>>();
            for (let i = 1; i < tokens.length; i++) {
                if (tokens[i].text.length > longest.len) {
                    longest.idx = i;
                    longest.len = tokens[i].text.length;
                }
                if (same.has(tokens[i].text)) {
                    same.get(tokens[i].text).push(i);
                } else {
                    same.set(tokens[i].text, [i]);
                }
            }
            let idx = same.get(tokens[longest.idx].text).pop();
            this._stringStream = this._stringStream.substr(tokens[idx].text.length);
            return tokens[idx];
        }
        throw new LexerError("Unexpected token: \""
            + this._stringStream.substr(0, 10) + "\"")
    }
}
////////////////////////////////////////////////////
// ***** 3. Core data structure: TreeNode **********
////////////////////////////////////////////////////
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
    /**
     * Chain invoking to set inner value.
     * Nice interface!
     */
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
    /**
     * Pack inner variables by providing getter methods.
     */
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
class Walker {
    private _obj: Object;

    listen(obj: Object): Walker {
        this._obj = obj;
        return this;
    }

    walk(type: string, node: TreeNode, enter: boolean = true) {
        if (enter && this._obj.hasOwnProperty("enter_" + type) && (this._obj["enter_" + type] instanceof Function)) {
            this._obj["enter_" + type](node);
        } else if (!enter && this._obj.hasOwnProperty("exit_" + type) && (this._obj["exit_" + type] instanceof Function)) {
            this._obj["exit_" + type](node);
        }
    }
}
////////////////////////////////////////////////////
// ***** 4. Grammar item definition ****************
////////////////////////////////////////////////////
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
/**
 * The {@code TreeNodeFactory} is prepared for
 * generating TreeNode.
 */
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
            console.info("   " + key);
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
        console.debug("Build node: " + type + "; recursion: " + this.recursionSignature.has(type));
        if (this.recursionSignature.has(type)) {
            /** Many limitation here! Maybe more code need here! */
            r.setRecursion(true)
                .setTemplate(this.grammar.get(type).recursionItems)
                .setOptionConcat(this.grammar.get(type).optionConcat);
        } else {
            r.setOptions(this.grammar.get(type).options);
        }
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
////////////////////////////////////////////////////
// ***** 5. Parser definition **********************
////////////////////////////////////////////////////
/**
 * {@code ParserTree}, key of grammar parsing.
 */
class ParserTree {
    /** from global */
    private root: TreeNode;
    private errorExpected: Array<string> = [];
    /** local record */
    private ptr: TreeNode;
    private factory: TreeNodeFactory = new TreeNodeFactory();
    private walker: Walker = new Walker();

    // @ts-ignore
    rule(signature: string, handle: Map<string, Array<string>>, recursion: Array<string> | null = null): void {
        this.factory.rule(signature, handle, recursion);
    }

    listen(o: Object): ParserTree {
        this.walker.listen(o);
        return this;
    }

    parse(s: string, grammarStart: string, lexer: Lexer): void {
        this.root = this.factory.build(grammarStart);
        this.ptr = this.root;
        let token: Token;
        lexer.set(s);
        while ((token = lexer.lex()).type != "EOF") {
            this.buildTree(this.factory, this.ptr, token);
        }
        this.buildTree(this.factory, this.ptr, token)
    }

    isOver(): boolean {
        return this.root.getUid() === this.ptr.getUid();
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

    private getTreeJsonInner(node: TreeNode): Object {
        let text: string;
        if (this.factory.isLeaf(node.getType())) {
            text = node.getType() + ":\"" + node.getText() + "\"";
        } else {
            text = node.getType();
        }
        let r: Object = {};
        r["name"] = text;
        if (node.getChildSize() !== 0) {
            let children: Array<Object> = [];
            for (let i = 0; i < node.getChildSize(); i++) {
                children.push(this.getTreeJsonInner(node.getChild(i)));
            }
            r["children"] = children;
        }
        return r;
    }

    getTreeJson(): Object {
        return this.getTreeJsonInner(this.root);
    }

    walkTree(node: TreeNode) {
        if (this.factory.isLeaf(node.getType())) return;
        // TODO: enter
        this.walker.walk(node.getType(), node);
        if (node.getChildSize() !== 0) {
            for (let i = 0; i < node.getChildSize(); i++) {
                this.walkTree(node.getChild(i));
            }
        }
        this.walker.walk(node.getType(), node, false);
        // TODO: exit
    }

    /**
     * Tree walker.
     */
    private buildTree(factory: TreeNodeFactory, treeNode: TreeNode, token: Token): void {
        if (treeNode.getChildSize() === 0) {
            if (treeNode.getTemplate() === null || treeNode.getTemplate().length === 0) {
                if (!treeNode.getOptions().has(token.type)) {
                    let exp: Array<string> = []
                    for (let e in treeNode.getOptions().keys()) {
                        exp.push(e);
                    }
                    console.error("Node: " + treeNode.getType());
                    throw new GrammarError("Unexpected: " + token.text + "(" + token.type +
                        "); expect: " + this.errorExpected.concat(exp).join(",") + ".");
                }
                treeNode.setTemplate(treeNode.getOptions().get(token.type));
            }
        } else if (treeNode.getChildSize() === treeNode.getTemplate().length) {
            if (!treeNode.isRecursion()) {
                if (treeNode === this.root) {
                    return;
                }
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
////////////////////////////////////////////////////
// ***** 6. Wrapper ********************************
////////////////////////////////////////////////////
/**
 * Wrapper of all logic.
 */
class Grammar {
    static lexer(grammar: string): Lexer {
        let r_lexer: Lexer = new Lexer();
        grammar.split("\n").forEach(line => {
            if (line.trim() === "") return;
            let group = line.split(":");
            if (group.length !== 2) {
                throw new LexerError("lexer string error");
            }
            let signature: string = group[0].replace(" ", "");
            let r_reg: string = group[1].trim();
            if (r_reg === null || r_reg.length === 0) {
                throw new LexerError("lexer rule error! invalid rule: " + line);
            }
            let regexp = eval("/" + r_reg + "/");
            r_lexer.rule(signature, regexp);
        });
        return r_lexer;
    }

    static parser(grammar: string): ParserTree {
        let parserTree: ParserTree = new ParserTree();
        grammar.replace("\n", "")
            .replace("\t", "")
            .split(";").filter(line => line != "")
            .forEach(line => {
                let split_0: Array<string> = line.replace("\n", "").split(":").filter(word => word != "")
                if (split_0.length != 2) {
                    throw new GrammarError("grammar string error");
                }
                let signature = split_0[0].replace(" ", "");
                let items: Array<string> = split_0[1].split("|");
                // @ts-ignore
                let m: Map<string, Array<string>> = new Map<string, Array<string>>();
                let isRecursion: boolean = false;
                let recursionItems: Array<string> | null = null;
                    let count: number = 0;
                for (let index in items) {
                    let s = items[index];
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
                    throw new GrammarError("grammar recursion rule error: only one recursion item(must) allowed! number: "
                        + count);
                }
                if (isRecursion) {
                    m.delete(recursionItems[0]);
                    parserTree.rule(signature, m, recursionItems);
                } else {
                    parserTree.rule(signature, m);
                }
        });
        return parserTree;
    }
}
/* Export for extern using */
// export {
//     Token,
//     Lexer,
//     LexerError,
//     GrammarError,
//     RuleType,
//     Rule,
//     RegRule,
//     TreeNode,
//     TreeNodeFactory,
//     ParserTree,
//     GrammarItem,
//     Grammar,
//     Walker
// };
////////////////////////////////////////////////////
// ******************** OVER ***********************
// ******************** OVER ***********************
// ******************** OVER ***********************
// ******************** OVER ***********************
// ******************** OVER ***********************
////////////////////////////////////////////////////