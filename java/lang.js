const clone = o => JSON.parse(JSON.stringify(o))

class Token {
    constructor(type, text) {
        this.type = type
        this.text = text
    }
}

class NonTerminalSymbol {
    constructor(token) {
        if (token.type !== 1)
            throw new Error("Non terminal symbol Error")
        this.text = token.text; this.type = token.type
    }
    tokenClauses = []
    isLeftRecursion = false
    allowedTermsHeader = new Set()
    askForHead(nTermsMapper, stopNonTermNames = new Set()) {
        if (stopNonTermNames.has(this.text)) return
        stopNonTermNames.add(this.text)

        if (this.allowedTermsHeader.size > 0) {
            // console.info(`[nTerm] ${this.text} has been searched, use cache. Stop conditions: ${JSON.stringify([...stopNonTermNames])}`)
            return new Set([...this.allowedTermsHeader])
        }

        // console.info(`[nTerm] ${this.text} hasn't been searched. Stop conditions: ${JSON.stringify([...stopNonTermNames])} Searching ...`)

        let res = new Set()
        for (let i = 0; i < this.tokenClauses.length; i++) {
            const tokenClauseHead = this.tokenClauses[i][0]
            if (tokenClauseHead.type === 0) {
                res.add(tokenClauseHead.text)
            } else if (tokenClauseHead.type === 1) {
                if (tokenClauseHead.text !== this.text) {
                    const arr = nTermsMapper.get(tokenClauseHead.text).askForHead(nTermsMapper, stopNonTermNames)
                    res = new Set([...res, ...arr])
                }
            }
        }
        res.forEach(i => this.allowedTermsHeader.add(i))
        return this.allowedTermsHeader
    }

    allowedTailHeader = new Set()
    askForTailHead(nTermsMapper) {
        if (!this.isLeftRecursion) return new Set()
        if (this.allowedTailHeader.size > 0) {
            // console.info(`[nTerm] ${this.text}'s tail has been searched, use cache.`)
            return this.allowedTailHeader
        }
        // console.info(`[nTerm] ${this.text}'s tail hasn't been searched. Searching ...`)
        let tailHeader = new Set()
        for (let i = 0; i < this.tokenClauses.length; i++) {
            const clause = this.tokenClauses[i]
            if (clause[0].text === this.text) {
                if (clause[1].type === 0) {
                    tailHeader = new Set([...tailHeader, clause[1].text])
                } else {
                    const many = nTermsMapper.get(clause[1].text).askForHead(nTermsMapper)
                    tailHeader = new Set([...tailHeader, ...many])
                }
            }
        }
        tailHeader.forEach(i => this.allowedTailHeader.add(i))
        return this.allowedTailHeader
    }
}

class BNFContext {
    constructor() { this.terms = new Set(); this.nonTerms = new Set() }
    addTerm(text) { this.terms.add(text) }
    addNonTerm(text) { this.nonTerms.add(text) }
    fixedStr(s, len = 10) {
        if (s.length < len) {
            let arr = []
            arr.push(s)
            for (let i = 0; i < (len - s.length); i++) {
                arr.push(' ')
            }
            return arr.join("")
        }
        return s
    }
    printContext() {
        console.info(`BNF context:\n  Term: ${[...this.terms]}\n  Non term: ${[...this.nonTerms]}`)
        console.info(`BNF generator:`)
        for (const nTermName of this.allNonTerms.keys()) {
            const self = this.allNonTerms.get(nTermName)
            const head = self.askForHead(this.allNonTerms)
            const tail = self.askForTailHead(this.allNonTerms)
            console.info(`  ${this.fixedStr(nTermName)}: left recursion: ${self.isLeftRecursion}; heads: ${[...head].join(",")}; optional appender: ${[...tail].join(",")}`)
        }
    }
    allNonTerms = new Map()
    parseOne(tokens, priority) {
        const nTermName = tokens[0].text
        if (!this.allNonTerms.has(nTermName)) {
            this.allNonTerms.set(nTermName, new NonTerminalSymbol(tokens[0]))
        }
        const nonTerm = this.allNonTerms.get(nTermName)
        let arr = []
        for (let i = 2; i < tokens.length; i++) {
            const token = tokens[i]
            if ((token.type === 4 && arr.length > 0) || i === tokens.length - 1) {
                if (arr[0].text === nTermName) {
                    nonTerm.isLeftRecursion = true
                }
                nonTerm.tokenClauses.push(clone(arr))
                arr.splice(0, arr.length)
            } else if (token.type !== 4) {
                arr.push(token)
            }
        }
    }
    parse(tokens) {
        let stream = tokens.map(tk => String.fromCharCode(tk.type + 65)).join("")
        const formReg = /^BC[AB]+(E[AB]+)*D/
        let priority = 0
        let counter = 0
        while (stream.length > 0) {
            let pre = stream.match(formReg)
            const n = pre === null ? 0 : pre[0].length
            if (n > 0) {
                this.parseOne(tokens.slice(counter, counter + n), priority++)
                counter += n
                stream = stream.substr(n)
                continue
            }
            throw new Error(`Form parse error!`)
        }
    }
}

class Lexer {
    lex(s) {
        console.info(`Lexing text received ${s.length} characters.`)
        /*
         * A term
         * B non term
         * C ::=
         * D ;
         * E |
         * AC[AB]+(|[AB]+)*D
         * */
        const regexes = [/^[A-Z_]+/, /^[a-zA-Z_]+/, /^::=/, /^;/, /^\|/, /^[ \r\n\t]+/]
        const tokens = []; let stream = s
        let chCounter = 0; let lineCounter = 0
        while(stream.length > 0) {
            let deal = false
            for (let i = 0; i < regexes.length; i++) {
                const regex = regexes[i]
                let pre = stream.match(regex)
                const n = pre === null ? 0 : pre[0].length
                if (n > 0) {
                    const text = stream.substr(0, n)
                    // console.info(`=> type: ${i} length: ${n} current: ${lineCounter}:${chCounter} ${text}`)
                    chCounter += n
                    lineCounter += text.split('\n').length - 1
                    tokens.push(new Token(i, text))
                    stream = stream.substr(n)
                    deal = true
                    break
                }
            }
            if (!deal) {
                let p = stream.indexOf('\n')
                let len = p === -1 ? 10 : p;
                throw new Error(`Lexer error, [${lineCounter}:${chCounter}]: ${stream.substr(0, Math.min(len, 10))}`)
            }
        }
        return tokens
    }
}

const main = () => {
    const bnfContext = new BNFContext()
    const tokens = new Lexer().lex("calcExpr ::= calcExpr ADD mulExpr | mulExpr ;"
        + "mulExpr ::= mulExpr MUL ATOM | ATOM;")
        .filter(token => token.type !== 5)
    tokens
        .forEach(token => {
            if (token.type === 1) {
                bnfContext.addNonTerm(token.text)
            } else if (token.type === 0) {
                bnfContext.addTerm(token.text)
            }
            // console.info(JSON.stringify(token))
        })
    bnfContext.parse(tokens)
    bnfContext.printContext()
}

main()