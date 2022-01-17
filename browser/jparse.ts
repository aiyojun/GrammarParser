/*
 * Copyright (c) 2022, aiyojun. All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES.
 *
 * This code is free software; At current stage, you can use it freely, without any risks.
 */

/**
 * jparse.ts is a grammar parsing tool written by typescript(This file). You can define lexer and
 * parser by yourself, and the way to create grammar rules is very easy and convenient.
 *
 * @author: aiyojun
 * If you have any question about this file, you can send mail to me.
 *
 * This file mainly include four parts: 1. lexer; 2. parser; 3. tree node; 4. grammar parser
 */
class utils {
    static set2array(s: Set<string>): Array<string> {
        let _r: Array<string> = [];
        for (let i in s) {_r.push(i);}
        return _r;
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// ******************************** 1. Exception definition ***************************************
///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * The {@code LexerError} will be thrown in token lexing stage, if any problem occur.
 */
class LexerError extends Error {constructor(message?: string) {super(message);}}
/**
 * The {@code GrammarError} will be thrown in AST tree building stage, if any problem occur.
 */
class GrammarError extends Error {constructor(message?: string) {super(message);}}
///////////////////////////////////////////////////////////////////////////////////////////////////
// ************************** 2. Lexer and related tools definition *******************************
///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * The {@code Token} is output after passing lexer. And now, i use the common method(type: string)
 * to represent inner grammar signature. Maybe enum is better.
 */
class Token {
    private readonly _text: string;
    private readonly _type: string;
    /**
     * The unique method to create {@code Token}.
     * @param text raw token string
     * @param type token type, defined by yourself.
     *          like: FIELD, KEYWORD
     */
    static build(type: string, text: string) {
        return new Token(type, text);
    }
    /**
     * Hide the only one constructor.
     */
    private constructor(type: string, text: string) {
        this._text = text;
        this._type = type;
    }
    /**
     * The standard member accessing is provided.
     */
    getText(): string {return this._text;}
    getType(): string {return this._type;}
}

/**
 * Lexer rule definition. Using regex to create Lexer rules!
 */
class RegRule {
    /**
     * Necessary uid generator.
     */
    private static uidGen = 0;
    /**
     * The following several variables are members of {@code RegRule}.
     */
    private readonly _reg : RegExp;
    private readonly _uid : number;
    private readonly _type: string;
    /**
     * The only way to create {@code RegRule}
     */
    static build(type: string, re: RegExp): RegRule {
        return new RegRule(type, re);
    }
    /**
     * Hide constructor of the class.
     */
    private constructor(type: string, re: RegExp) {
        this._uid = RegRule.uidGen++;
        this._reg = re;
        this._type = type;
    }
    /**
     * Outer accessing for members.
     */
    getReg() : RegExp {return this._reg;}
    getUid() : number {return this._uid;}
    getType(): string {return this._type;}
}
/**
 * The {@code Lexer} is a important part of GrammarParser.
 */
class Lexer {
    private _stream: string;
    private _rules: Array<RegRule> = [];
    /**
     * Set string stream for {@code Lexer}
     */
    set(s: string): Lexer {this._stream = s; return this;}
    /**
     * Add rule for {@code Lexer}.
     */
    rule(tokenName: string, regex: RegExp): Lexer {
        this._rules.push(RegRule.build(tokenName, regex));
        return this;
    }
    /**
     * Core method of {@code Lexer}, extract tokens from stream step by step.
     */
    lex(): Token {
        if (this._stream.length === 0)
            return Token.build("EOF", "");
        let tokens: Array<Token> = []
        for (let i: number = 0; i < this._rules.length; i++) {
            let r: Array<string> = this._stream.match(this._rules[i].getReg())
            if (r !== null) {
                tokens.push(Token.build(this._rules[i].getType(), r[0]))
            }
        }
        for (let index in tokens) {
            if (tokens[index].getType() === "skip") {
                this._stream = this._stream.substr(tokens[index].getText().length)
                return this.lex();
            }
        }
        if (tokens.length == 1) {
            this._stream = this._stream.substr(tokens[0].getText().length)
            return tokens[0]
        }
        if (tokens.length > 1) {
            let longest = {
                idx: 0,
                len: tokens[0].getText().length
            };
            // @ts-ignore
            let same: Map<string, Array<number>> = new Map<string, Array<number>>();
            for (let i = 1; i < tokens.length; i++) {
                if (tokens[i].getText().length > longest.len) {
                    longest.idx = i;
                    longest.len = tokens[i].getText().length;
                }
                if (same.has(tokens[i].getText())) {
                    same.get(tokens[i].getText()).push(i);
                } else {
                    same.set(tokens[i].getText(), [i]);
                }
            }
            let idx = same.get(tokens[longest.idx].getText()).pop();
            this._stream = this._stream.substr(tokens[idx].getText().length);
            return tokens[idx];
        }
        throw new LexerError("Unexpected token: \""
            + this._stream.substr(0, 10) + "\"")
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************** 3. Core data structure: TreeNode ********************************
///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * {@code TreeNode} is core data structure of abstract syntax tree(AST).
 * The target of all the codes is to build the whole AST for users.
 * Then, users can do what they want by using the AST.
 * Of course, we also provide a way to help user to take advantage of the AST.
 * {@see Walker}.
 */
class TreeNode {
    /**
     * All the necessary information to build a tree.
     * The Kept uid variable is to debug AST. Do not remove it!
     */
    private _uid      : number          = 0;
    private _parent   : TreeNode | null = null;
    private _child    : Array<TreeNode> = [];
    /**
     * The following are something to help to reduce the context.
     */
    private _text     : string          = "";
    private _type     : string          = "";
    private _template : Array<string>   = [];
    private _law      : Law;
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
    setLaw(law: Law): TreeNode {this._law = law; return this;}
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
    getLaw(): Law {return this._law;}
}
/**
 * A standard listener pattern to take advantage of AST.
 */
class Walker {
    /**
     * Listener object provided by user. However, you have to create it by the common schema, like:
     * {
     *    "enter_xxx": () => {}, "exit_xxx": () => {},
     *    // one entry, one exit.
     *    // write the rest listener functions of grammar rules.
     * }
     */
    private _obj: Object;
    /**
     * Set inner listener object.
     */
    listen(obj: Object): Walker {
        this._obj = obj;
        return this;
    }
    /**
     * Inner invoking! Pick the related listener function and execute, when the pointer is passing a node.
     */
    walk(type: string, node: TreeNode, enter: boolean = true) {
        if (enter && this._obj.hasOwnProperty("enter_" + type)
            && (this._obj["enter_" + type] instanceof Function)) {
            this._obj["enter_" + type](node);
        } else if (!enter && this._obj.hasOwnProperty("exit_" + type)
            && (this._obj["exit_" + type] instanceof Function)) {
            this._obj["exit_" + type](node);
        }
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// ********************************* 4. Grammar item definition ***********************************
///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * We call the grammar item {@code Law}! It's a rich type, including many functions which can help
 * to build the ASTree! You can explore the inner logic if you are interested in it!
 */
class Law {
    private static uidGen: number = 0;
    private parser: Parser;
    private readonly uid: number;
    private readonly signature: string;
    private readonly leftLoop: Array<number>;
    private readonly items: Array<Array<string>>;
    // @ts-ignore
    private indexedHeaders: Map<string, Array<number>>;
    /**
     * The method to create a law by parsing strings of grammar rule.
     */
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
                .map(str => str.trim())
                .filter(item => item !== "")
                .map(itemStr =>
                    itemStr.split(" ")
                        .map(s => s.replace("\n", ""))
                        .filter(s => s !== ""))
        );
    }
    /**
     * The hidden constructor. Didn't provide way to create {@code Law} for user.
     */
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
                throw new GrammarError("invalid grammar, "
                    + signature + ": " + signature + ";");
            }
            if (indexSeq.length > 0 && indexSeq[0] === 0) {
                this.leftLoop.push(i);
            }
        }
    }
    /**
     * A part of getter and setter.
     */
    setParser(parser: Parser): Law {this.parser = parser; return this;}
    getUid(): number {return this.uid;}
    getSignature(): string {return this.signature;}
    getItems(): Array<Array<string>> {return this.items;}
    /**
     * Grab the headers of every rule.
     */
    getLeaders(): Array<string> {
        let _r: Array<string> = [];
        this.items.map(arr => arr[0]).filter(s => s !== this.signature).forEach(
            s => {if (_r.indexOf(s) === -1) _r.push(s);}
        )
        return _r;
    }
    /**
     * Stage analysis.
     */
    takeOver(factory: TreeNodeFactory, pointer: TreeNode, token: Token): TreeNode {
        let concat: Array<number> = [];
        let sameAs: Array<number> = [];
        for (let i = 0; i < this.items.length; i++) {
            let itemSeq = this.items[i];
            let decision = Law.satisfy(pointer.getTemplate(), itemSeq);
            if (decision === 1) {
                concat.push(i);
            } else if (decision === 2) {
                sameAs.push(i);
            }
        }
        if (concat.length === 0 && sameAs.length === 0) {
            throw new GrammarError("logical error");
        }
        console.debug("Take over analysis: ");
        console.debug("··node: " + pointer.getType());
        console.debug("··template: " + pointer.getTemplate().join(", "));
        console.debug("··concat.length=" + concat.length + "; same.length="+ sameAs.length);
        console.debug("··law: " + this.getSignature());
        console.debug("····grammar(items): ");
        this.items.forEach(arr => {console.debug("······" + arr.join(", "))});
        if (concat.length === 0) { // sameAs.length > 0
            if (this.leftLoop.length === 0) {
                return pointer.getParent();
            }
            // left recursion rule reducing
            let sharedPartition = this.shared(this.leftLoop);
            if (this.leftLoop.length > 1 && sharedPartition.length > 1 &&
                this.parser.expect(this.items[this.leftLoop[0]][1]).has(token.getType())) {
                let node = factory
                    .build(pointer.getType())
                    .setParent(pointer.getParent())
                    .addChild(pointer)
                    .setTemplate(sharedPartition);
                pointer.getParent()
                    .replaceChild(pointer.getParent().getChildSize() - 1, node);
                pointer.setParent(node);
                return node;
            }
            // shared partition length === 1
            for (let i = 0; i < this.leftLoop.length; i++) {
                if (this.parser.expect(this.items[this.leftLoop[i]][1])
                    .has(token.getType())) {
                    let node = factory
                        .build(pointer.getType())
                        .setParent(pointer.getParent())
                        .addChild(pointer)
                        .setTemplate(this.items[this.leftLoop[i]]);
                    pointer.getParent()
                        .replaceChild(pointer.getParent().getChildSize() - 1, node);
                    pointer.setParent(node);
                    return node;
                }
            }
            // left recursion with simple rule to reduce
            return pointer.getParent();
        }
        // concat type
        for (let i = 0; i < concat.length; i++) {
            if (this.parser.expect(this.items[concat[i]][pointer.getTemplate().length])
                .has(token.getType())) {
                pointer.setTemplate(this.items[concat[i]]);
                return pointer;
            }
        }
        if (sameAs.length === 0) {
            throw new GrammarError("grammar error, " + token.getText() + "; after: " + this.parser.getRecord());
        }
        // reducing type
        return pointer.getParent();
    }
    /**
     * Initialization of template.
     */
    buildingTemplate(token: Token): Array<string> {
        for (let [header, indices] of this.indexedHeaders) {
            if (this.signature === header) continue;
            if (this.parser.expect(header).has(token.getType())) {
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
        console.debug("++++++++++++++++++++++")
        console.debug("Build template failed:");
        console.debug("··node: " + this.parser.pointer.getType());
        console.debug("··law: " + this.getSignature());
        console.debug("····grammar(items): ");
        this.items.forEach(arr => {console.debug("      " + arr.join(", "))});
        console.debug("··expect:");
        for (let [header, indices] of this.indexedHeaders) {
            console.debug("····" + header + "[" + indices.join(",") + "] "
                + utils.set2array(this.parser.expect(header)).join(", "));
        }
        throw new GrammarError("grammar error, " + token.getText() + "(" + token.getType()
            + "); law: " + this.getSignature() + "; after: " + this.parser.getRecord());
    }
    /**
     * Inner method.
     */
    private static satisfy(accepted: Array<string>, seq: Array<string>): number {
        if (accepted.length > seq.length) return 0;
        for (let i = 0; i < accepted.length; i++) {
            if (accepted[i] !== seq[i]) {
                return 0;
            }
        }
        return accepted.length !== seq.length ? 1 : 2;
    }
    /**
     * Inner method.
     */
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
///////////////////////////////////////////////////////////////////////////////////////////////////
// ************************************* 5. Parser definition *************************************
///////////////////////////////////////////////////////////////////////////////////////////////////
class LawNode {
    // @ts-ignore
    static root: LawNode;
    private static _nodes: Map<string, LawNode> = new Map<string, LawNode>();

    private _name: string;
    private _parent: LawNode;

    private constructor(name: string, parent: LawNode = null) {
        this._name = name;
        this._parent = parent;
    }

    static add(name: string, parentName: string) {
        if (!LawNode._nodes.has(parentName)) {
            LawNode._nodes.set(parentName, new LawNode(parentName));
        }
        if (LawNode._nodes.has(name)) {
            throw new GrammarError("repeat LawNode! " + name);
        }
        LawNode._nodes.set(name, new LawNode(name, LawNode._nodes.get(parentName)));
        // if (parentName === null) {
        //     LawNode._nodes.clear();
        //     let lawNode = new LawNode(name, null);
        //     LawNode.root = lawNode;
        //     LawNode._nodes.set(name, lawNode);
        //     return;
        // }
        // if (!LawNode._nodes.has(parentName)) {
        //     throw new GrammarError("cannot find parent law: " + parentName + ", for " + name);
        // }
        // LawNode._nodes.set(name, new LawNode(name, LawNode._nodes.get(parentName)));
    }

    static getChain(name: string): Array<string> {
        let _r: Array<string> = [];
        if (LawNode._nodes.has(name)) {
            let lawNode: LawNode = LawNode._nodes.get(name)._parent;
            while (lawNode !== null) {
                _r.push(lawNode._name);
                lawNode = lawNode._parent;
            }
        }
        return _r;
    }
}
/**
 * {@code Parser} provides all functions of parsing grammar.
 * First, you should create a lexer and a parser by passing the related grammar rules context.
 * Then, specify the start rule and move the lexer to the parser to run parsing process.
 * Finally, bind the listener which is defined by yourself.
 */
class Parser {
    // @ts-ignore
    private grammar : Map<string, Law>;
    // @ts-ignore
    private signSeq : Set<string>;
    private root    : TreeNode;
            pointer : TreeNode;
    private factory : TreeNodeFactory;
    private walker  : Walker;
    private deepest : number = 0;
    private records : Array<Token> = [];
    // private disable : Set<string>;
    // private fromBeg : Set<string>;
    getDepth(): number {return this.deepest;}
    /**
     * Provide a entry.
     */
    static build(context: string): Parser {
        let _r = new Parser();
        // @ts-ignore
        _r.grammar = new Map<string, Law>();
        // @ts-ignore
        _r.signSeq = new Set<string>();
        // _r.fromBeg = new Set<string>();
        // _r.disable = new Set<string>();
        // let index = 0;
        context.replace("\n", "")
            .split(";")
            .map(every => every.replace("\n", "").replace("\t", "").trim())
            .filter(every => every !== "")
            .forEach(every => {
                let law = Law.parse(every).setParser(_r);
                // if (index === 0)
                //     LawNode.add(law.getSignature());
                // index++;
                _r.grammar.set(law.getSignature(), law);
                _r.signSeq.add(law.getSignature());
                // law.getLeaders().forEach(
                //     leader => {
                //         _r.fromBeg.add(law.getSignature() + "." + leader);
                //         _r.disable.add(leader + "." + law.getSignature());
                //         LawNode.add(leader, law.getSignature());
                //         LawNode.getChain(leader).forEach(trace => {
                //             _r.disable.add(leader + "." + trace)
                //         });
                //     });
            });
        _r.factory = new TreeNodeFactory().setGrammar(_r.grammar);
        _r.walker  = new Walker();
        return _r;
    }
    // static hasChain(sign: string, container: Set<string>): string | null {
    //     for (let every of container) {
    //         let everySigns = every.split(".");
    //         if (sign === everySigns[0]) {
    //             return everySigns[1];
    //         }
    //     }
    //     return null;
    // }
    // static getChain(sign: string, container: Set<string>) {
    //     let _r: Array<string> = [];
    //     let every: string | null;
    //     while ((every = Parser.hasChain(sign, container)) !== null) {
    //         _r.push(every);
    //     }
    //     return _r;
    // }
    /**
     * You know: hide!
     */
    private constructor() {}
    /**
     * Reserved for debugging.
     */
    printGrammar() {
        console.debug("Grammar: " + this.grammar.size);
        for (let signature of this.grammar.keys()) {
            console.debug(
                signature
                + ": "
                + this.grammar
                    .get(signature)
                    .getItems()
                    .map(vec => vec.join(" "))
                    .join("\n\t | ")
            );
        }
    }
    /**
     * Put this method in {@code Parser} class. Because it needs to use the shared information.
     */
    // @ts-ignore
    expect(signature: string): Set<string> {
        // @ts-ignore
        let _r: Set<string> = new Set<string>();
        if (!this.signSeq.has(signature)) {
            _r.add(signature);
            return _r;
        }
        // @ts-ignore
        // let disabled: Set<string> = new Set<string>();
        // console.debug(signature + " \tget leaders: " + this.grammar.get(signature).getLeaders().join(", "))
        this.grammar.get(signature).getLeaders().forEach(leader => {
            // let hash = signature + "." + leader;
            // if (this.disable.has(hash)) {
            //     throw new GrammarError("grammar error, invalid cycle rule: " + hash);
            // }
            // if (this.signSeq.has(leader) && disabled.has(hash)) {
            //     console.info("disabled cycle rules:\n");
            //     for (let k of disabled) {
            //         console.info("  " + k);
            //     }
            //     throw new GrammarError("grammar error, invalid cycle rule: " + hash);
            // } else {
            //     disabled.add(hash);
            // }
            if (this.signSeq.has(leader)) {
                this.expect(leader).forEach(every => _r.add(every));
            } else {
                // console.debug("  signature: " + signature + "; header: " + leader);
                _r.add(leader);
            }
        });
        return _r;
    }

    private record(token: Token) {
        let recordSize = 5;
        this.records.push(token);
        if (this.records.length > recordSize) {
            let numberToRemove = this.records.length - recordSize;
            this.records = this.records.slice(numberToRemove);
        }
    }
    getRecord(): string {
        return this.records.map(token => token.getText()).join(" ");
    }
    /**
     * Public interface to run parsing process.
     */
    parse(s: string, grammarStart: string, lexer: Lexer): void {
        this.root = this.factory.build(grammarStart);
        this.pointer = this.root;
        let token: Token;
        lexer.set(s);
        while ((token = lexer.lex()).getType() != "EOF") {
            this.buildTree(token);
            this.record(token);
        }
        this.buildTree(token)
    }
    /**
     * Method to ask whether the ASTree building is over.
     */
    isOver(): boolean {
        console.debug("Over check:");
        console.debug(
            "··root: " + this.root.getType() + "[" + this.root.getUid() + "]; " +
            "pointer: " + this.pointer.getType() + "[" + this.pointer.getUid() + "]"
        );
        return this.root.getUid() === this.pointer.getUid();
    }
    /**
     * Obtain the root of ASTree. Then, you can do what you want!
     */
    getTree(): TreeNode {
        return this.root;
    }
    /**
     * Listener binding.
     */
    listen(o: Object): Parser {
        this.walker.listen(o);
        return this;
    }
    /**
     * ASTree traversing and the custom listener functions executing.
     */
    walkTree(node: TreeNode, depth: number = 1) {
        if (this.deepest < depth) this.deepest = depth;
        if (!this.signSeq.has(node.getType())) return;
        // TODO: enter
        this.walker.walk(node.getType(), node);
        if (node.getChildSize() !== 0) {
            for (let i = 0; i < node.getChildSize(); i++) {
                this.walkTree(node.getChild(i), depth + 1);
            }
        }
        this.walker.walk(node.getType(), node, false);
        // TODO: exit
    }
    /**
     * If you want to look the ASTree structure by echarts,
     * you can invoke the method to obtain the Json data of the tree!
     */
    getTreeJson(): Object {
        return this.getTreeJsonInner(this.root);
    }
    /**
     * Prepared for getTreeJson() method, inner invoking!
     */
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
        if (this.pointer.getTemplate().length === 0) {
            // TODO: init template
            this.pointer.setTemplate(this.pointer.getLaw().buildingTemplate(token));
            console.debug("Node template init result:");
            console.debug("··node: " + this.pointer.getType());
            console.debug("··template: " + this.pointer.getTemplate().join(", "));
            console.debug("··next token: " + token.getText() + "(" + token.getType() + ")");
        } else if (this.pointer.getChildSize() === this.pointer.getTemplate().length) {
            console.debug("[>>] Reduce tree(before law take over):");
            console.debug("··node: " + this.pointer.getType());
            console.debug("··template: " + this.pointer.getTemplate().join(", "));
            console.debug("··next token: " + token.getText() + "(" + token.getType() + ")");
            if (this.isOver() && token.getType() === "EOF") return;
            this.pointer = this.pointer.getLaw().takeOver(this.factory, this.pointer, token);
            console.debug("[<<] Reduce tree(after):");
            console.debug("··node: " + this.pointer.getType());
            console.debug("··template: " + this.pointer.getTemplate().join(", "));
            this.buildTree(token);
            return;
        }
        // TODO:
        let expect = this.pointer.getTemplate()[this.pointer.getChildSize()];
        if (this.signSeq.has(expect)) {
            this.pointer = this.factory.build(expect).setParent(this.pointer);
            this.pointer.getParent().addChild(this.pointer);
            this.buildTree(token);
        } else if (token.getType() == expect) {
            this.pointer.addChild(this.factory.leaf(this.pointer, token));
        } else {
            console.debug("Normal logic error:");
            console.debug("··node: " + this.pointer.getType());
            console.debug("··template: " + this.pointer.getTemplate().join(", "));
            console.debug("··children: " + this.pointer.getChildSize());
            console.debug("··expect: " + expect);
            throw new GrammarError(
                "unexpected token \"" + token.getText() + "\"(" + token.getType() +
                "); expect: (" + expect + "); after: " + this.getRecord()
            );
        }
    }
}

/**
 * The {@code TreeNodeFactory} is prepared for generating TreeNode.
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
    /**
     * Way to create middle node of ASTree.
     */
    build(type: string): TreeNode {
        return new TreeNode()
            .setUid(++TreeNodeFactory.uuidGenerator)
            .setType(type)
            .setLaw(this.grammar.get(type))
            ;
    }
    /**
     * Way to create leaf node of ASTree.
     */
    leaf(p: TreeNode, token: Token): TreeNode {
        return new TreeNode()
            .setUid(++TreeNodeFactory.uuidGenerator)
            .setParent(p)
            .setText(token.getText())
            .setType(token.getType())
            ;
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************************** 6. Wrapper ******************************************
///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Wrapper of all logic.
 */
class Grammar {
    static lexer(grammar: string): Lexer {
        let r_lexer: Lexer = new Lexer();
        grammar.split("\n").forEach(line => {
            if (line.trim() === "") return;
            let group = [];// = line.split(":");
            let colon = line.indexOf(":");
            if (colon < 1 || colon > line.length - 2) {
                throw new LexerError("lexer string error, " + line);
            }
            group.push(line.substr(0, colon));
            group.push(line.substr(colon + 1));
            // if (group.length !== 2) {
            //     throw new LexerError("lexer string error, " + line);
            // }
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
// export { Token, TreeNode, Lexer, Parser, LexerError, GrammarError, Grammar };
///////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************************** OVER ************************************************
///////////////////////////////////////////////////////////////////////////////////////////////////