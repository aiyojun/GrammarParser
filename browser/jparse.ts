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

    set(s: string) {this._stringStream = s}

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
    private _law: Law;
    // accepted: Array<string> = [];
    /** helper */
    // @ts-ignore
    // private _options: Map<string, Array<string>> | null = null;
    // @ts-ignore
    // private _optionConcat: Map<string, Array<string>> | null = null;
    // private _isRecursion: boolean = false;
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
    setLaw(law: Law): TreeNode {this._law = law; return this;}
    setTemplate(t: Array<string>): TreeNode {this._template = t; return this;}
    // @ts-ignore
    // setOptions(o: Map<string, Array<string>>): TreeNode {this._options = o; return this;}
    // @ts-ignore
    // setOptionConcat(c: Map<string, Array<string>>): TreeNode {this._optionConcat = c; return this;}
    // setRecursion(r: boolean): TreeNode {this._isRecursion = r; return this;}
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
    getLaw(): Law {return this._law;}
    getTemplate(): Array<string> {return this._template;}
    // @ts-ignore
    // getOptions(): Map<string, Array<string>> | null {return this._options;}
    // @ts-ignore
    // getOptionConcat(): Map<string, Array<string>> | null {return this._optionConcat;}
    // isRecursion(): boolean {return this._isRecursion;}
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
class Law {
    private static uidGen: number = 0;
    private parser: Parser;
    private readonly leftLoop: Array<number>;
    private readonly uid: number;
    private readonly signature: string;
    private readonly items: Array<Array<string>>;
    // @ts-ignore
    private indexedHeaders: Map<string, Array<number>>;

    private constructor(
        signature: string,
        items: Array<Array<string>>
    ) {
        this.uid = Law.uidGen++;
        this.items = items;
        this.signature = signature;
        this.leftLoop = [];
        // @ts-ignore
        this.indexedHeaders = new Map<string, Array<number>>();
        for (let i = 0; i < items.length; i++) {
            let itemSeq = items[i];
            if (this.indexedHeaders.has(itemSeq[0])) {
                this.indexedHeaders.get(itemSeq[0]).push(i);
            } else {
                this.indexedHeaders.set(itemSeq[0], [i]);
            }
            let indexSeq: Array<number> = [];
            for (let j = 0; j < itemSeq.length; j++) {
                if (itemSeq[j] === signature) {
                    indexSeq.push(j);
                }
            }
            // if (indexSeq.length > 1 || (indexSeq.length === 1 && indexSeq[0] !== 0)) {
            //     throw new GrammarError("left recursion support only");
            // }
            if (items.length === 1 && indexSeq.length === 1) {
                throw new GrammarError("invalid grammar, " + signature + ": " + signature + ";");
            }
            if (indexSeq.length > 0 && indexSeq[0] === 0) {
                this.leftLoop.push(i);
            }
        }
    }

    setParser(parser: Parser): Law {this.parser = parser; return this;}

    getUid(): number {return this.uid;}
    getSignature(): string {return this.signature;}
    getItems(): Array<Array<string>> {return this.items;}

    getLeaders(): Array<string> {
        return this.items.map(arr => arr[0]).filter(s => s !== this.signature);
    }

    static parse(str: string): Law {
        str = str.trim();
        let p = str.indexOf(":");
        if (p <= 0 || p >= str.length) {
            throw new GrammarError("parse law failed");
        }
        let signature = str.substr(0, p).trim();
        return new Law(
            signature,
            str.substr(p + 1)
                .trim()
                .split("|")
                .map(itemStr =>
                    itemStr.split(" ")
                        .filter(s => s !== ""))
        );
    }

    takeOver(factory: TreeNodeFactory, ptr: TreeNode, token: Token): TreeNode { //take over predict
        let concat: Array<number> = [];
        let sameAs: Array<number> = [];
        for (let i = 0; i < this.items.length; i++) {
            let itemSeq = this.items[i];
            let decision = Law.satisfy(ptr.getTemplate(), itemSeq);
            if (decision === 1) {
                concat.push(i);
            } else if (decision === 2) {
                sameAs.push(i);
            }
        }
        if (concat.length === 0 && sameAs.length === 0) {
            throw new GrammarError("logical error");
        }
        if (concat.length === 0) { // sameAs.length > 0
            return ptr.getParent();
        }
        if (sameAs.length === 0) {
            for (let i = 0; i < concat.length; i++) {
                if (this.parser.expect(this.items[concat[i]][ptr.getTemplate().length]).has(token.type)) {
                    ptr.setTemplate(this.items[concat[i]]);
                    return ptr;
                }
            }
            throw new GrammarError("grammar error, " + token.text);
        }
        // concat.length > 0 && sameAs.length > 0
        if (this.leftLoop.length > 0) {
            // left recursion rule reducing
            for (let i = 0; i < this.leftLoop.length; i++) {
                if (this.parser.expect(this.items[this.leftLoop[i]][1]).has(token.type)) {
                    let node = factory
                        .build(ptr.getType())
                        .setParent(ptr.getParent())
                        .addChild(ptr)
                        .setTemplate(this.items[this.leftLoop[i]]);
                    ptr.getParent().replaceChild(ptr.getParent().getChildSize() - 1, node);
                    ptr.setParent(node);
                    return node;
                }
            }
            // left recursion with simple rule to reduce
            return ptr.getParent();
        }
        // concat type
        for (let i = 0; i < concat.length; i++) {
            if (this.parser.expect(this.items[concat[i]][ptr.getTemplate().length]).has(token.type)) {
                ptr.setTemplate(this.items[concat[i]]);
                return ptr;
            }
        }
        // reducing type
        return ptr.getParent();
    }

    initAccept(token: Token): Array<string> {
        for (let [header, indices] of this.indexedHeaders) {
            if (this.parser.expect(header).has(token.type)) {
                if (indices.length === 1) {
                    return this.items[indices[0]];
                } else {
                    let share = this.shared(indices);
                    if (share.length === 0) {
                        throw new GrammarError("");
                    }
                    return share;
                }
            }
        }
        throw new GrammarError("grammar error, " + token.text);
    }

    private static satisfy(accepted: Array<string>, seq: Array<string>): number {
        if (accepted.length > seq.length) return 0;
        for (let i = 0; i < accepted.length; i++) {
            if (accepted[i] !== seq[i]) {
                return 0;
            }
        }
        return accepted.length !== seq.length ? 1 : 2;
    }

    private shared(indices: Array<number>): Array<string> {
        let minSize = this.items[indices[0]].length;
        for (let i = 1; i < indices.length; i++) {
            if (this.items[indices[i]].length < minSize) {
                minSize = this.items[indices[i]].length;
            }
        }
        let _r: Array<string> = [];
        for (let i = 0; i < minSize; i++) {
            let k = this.items[indices[0]][i];
            for (let j = 1; j < indices.length; j++) {
                if (k !== this.items[indices[j]][i]) {
                    return _r;
                }
            }
            _r.push(k);
        }
        return _r;
    }
}

class Parser {
    // @ts-ignore
    private grammar: Map<string, Law>;
    // @ts-ignore
    private signSeq: Set<string>;
    private ptr: TreeNode;
    private root: TreeNode;
    private factory: TreeNodeFactory;
    private walker: Walker;

    private constructor() {}

    static build(context: string): Parser {
        let _r = new Parser();
        // @ts-ignore
        _r.grammar = new Map<string, Law>();
        // @ts-ignore
        _r.signSeq = new Set<string>();
        context.replace("\n", "")
            .split(";")
            .map(every => every.replace("\n", "").trim())
            .filter(every => every !== "")
            .forEach(every => {
                console.debug("(law string)> " + every);
                let law = Law.parse(every).setParser(_r);
                _r.grammar.set(law.getSignature(), law);
                _r.signSeq.add(law.getSignature());
        });
        _r.factory = new TreeNodeFactory().setGrammar(_r.grammar);
        _r.walker  = new Walker();
        return _r;
    }

    printGrammar() {
        console.info("Grammar: " + this.grammar.size);
        console.info("Grammar: " + this.grammar.keys());
        for (let key in this.grammar.keys()) {
            console.info("key: " + key);
        }
        for (let signature of this.grammar.keys()) {
            console.info(">> xx");
            console.info(signature + ": \n\t" + this.grammar.get(signature).getItems().map(vec => vec.join(" ")).join("\n\t | "));
        }
    }

    // @ts-ignore
    expect(signature: string): Set<string> {
        // @ts-ignore
        let _r: Set<string> = new Set<string>();
        // @ts-ignore
        let disabled: Set<string> = new Set<string>();
        this.grammar.get(signature).getLeaders().forEach(leader => {
            let hash = leader + "." + signature;
            if (disabled.has(hash)) {
                throw new GrammarError("");
            } else {
                disabled.add(hash);
            }
            if (this.signSeq.has(leader)) {
                this.expect(leader).forEach(every => _r.add(every));
            } else {
                _r.add(leader);
            }
        });
        return _r;
    }

    parse(s: string, grammarStart: string, lexer: Lexer): void {
        this.root = this.factory.build(grammarStart);
        this.ptr = this.root;
        let token: Token;
        lexer.set(s);
        while ((token = lexer.lex()).type != "EOF") {
            this.buildTree(token);
        }
        this.buildTree(token)
    }

    isOver(): boolean {
        return this.root.getUid() === this.ptr.getUid();
    }

    getTree(): TreeNode {
        return this.root;
    }

    listen(o: Object): Parser {
        this.walker.listen(o);
        return this;
    }

    walkTree(node: TreeNode) {
        if (!this.signSeq.has(node.getType())) return;
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

    getTreeJson(): Object {
        return this.getTreeJsonInner(this.root);
    }

    private getTreeJsonInner(node: TreeNode): Object {
        let text: string;
        if (!this.signSeq.has(node.getType())) {
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

    private buildTree(token: Token): void {
        if (this.ptr.getTemplate().length === 0) {
            // TODO: init accepted
            this.ptr.setTemplate(this.ptr.getLaw().initAccept(token));
        } else if (this.ptr.getChildSize() === this.ptr.getTemplate().length) {
            this.ptr = this.ptr.getLaw().takeOver(this.factory, this.ptr, token);
            this.buildTree(token);
            return;
        }
        // TODO:
        let expect = this.ptr.getTemplate()[this.ptr.getChildSize()];
        if (this.signSeq.has(expect)) {
            this.ptr = this.factory.build(expect).setParent(this.ptr);
            this.ptr.getParent().addChild(this.ptr);
            this.buildTree(token);
        } else if (token.type === expect) {
            this.ptr.addChild(this.factory.leaf(this.ptr, token));
        } else {
            throw new GrammarError("");
        }
    }
}

/**
 * The {@code TreeNodeFactory} is prepared for
 * generating TreeNode.
 */
class TreeNodeFactory {
    private static uuidGenerator: number = 0;
    // @ts-ignore
    private grammar: Map<string, Law>;
    // @ts-ignore
    setGrammar(grammar: Map<string, Law>): TreeNodeFactory {
        this.grammar = grammar;
        return this;
    }

    build(type: string): TreeNode {
        return new TreeNode()
            .setUid(++TreeNodeFactory.uuidGenerator)
            .setType(type)
            .setLaw(this.grammar.get(type))
            ;
    }

    leaf(p: TreeNode, token: Token): TreeNode {
        return new TreeNode()
            .setUid(++TreeNodeFactory.uuidGenerator)
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

    static parser(grammar: string): Parser {
        return Parser.build(grammar);
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