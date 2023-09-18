


type StrSet = Set<string>;

type Production = Array<string>;

interface Item {
    prod: Production;
    a: string;
}

type ItemSet = Set<Item>;

type Family = Set<ItemSet>;

type Grammar = Array<Array<string>>;

function SetOf<T>(...args: T[]) {
    const r: Set<T> = new Set
    args.forEach(x => r.add(x))
    return r
}

function isNTS(s: string) { return /[A-Z]/.test(s) }



function isNonTerminal(s: string) {
    return /[A-Z]/.test(s)
}

function strItem(item: Item) { return item.prod[0] + " -> " + item.prod.slice(1).join(' ') + " , " + item.a }

function classify(grammar: Grammar, nts: Set<string>, ts: Set<string>) {
    for (let i = 0; i < grammar.length; i++) {
        const prod = grammar[i]
        nts.add(prod[0])
        // console.info(typeof prod)
        prod.slice(1).forEach(s => {
            if (isNTS(s))
                nts.add(s)
            else
                ts.add(s)
        })
    }
    return nts
}

function strProduction(prod: Production) { return prod[0] + " -> " + prod.slice(1).join(" ") }

function itemSetContains(itemSet: ItemSet, item: Item) {
    for (const _item of itemSet) {
        if (strProduction(_item.prod) === strProduction(item.prod) && _item.a === item.a)
            return true
    }
    return false
}

function isOneState(state0: ItemSet, state1: ItemSet) {
    if (state0.size !== state1.size) return false
    for (const item of state0) {
        if (!itemSetContains(state1, item))
            return false
    }
    return true
}

function familyContains(family: Family, itemSet: ItemSet) {
    for (const member of family) {
        if (isOneState(member, itemSet))
            return true
    }
    return false
}

function subscript(C0: Family, C1: Family, I: ItemSet) {
    const Z0 = Array.from(C0)
    for (let i = 0; i < Z0.length; i++) {
        if (isOneState(Z0[i], I))
            return i
    }
    const Z1 = Array.from(C1)
    for (let i = 0; i < Z1.length; i++) {
        if (isOneState(Z1[i], I))
            return Z0.length + i
    }
    return -1
}

function snOfProduction(G: Grammar, prod: Production) {
    // console.info("production sn : ", strProduction(prod))
    return G.map(x => strProduction(x)).indexOf(strProduction(prod))
}

function getNextTerminalsOf(itemSet: ItemSet) {
    const term: StrSet = new Set
    for (const item of itemSet) {
        const i_dot = item.prod.indexOf('·')
        if (i_dot === item.prod.length - 1 || isNTS(item.prod[i_dot + 1])) continue
        term.add(item.prod[i_dot + 1])
    }
    return term
}

function first(G: Grammar, s: string) {
    const pack: Set<string> = new Set(G.filter(prod => prod[0] === s).map(prod => prod[1]))
    for (;;) {
        const app: Set<string> = new Set
        for (const x of pack) {
            G.filter(prod => prod[0] === x)
                .map(prod => prod[1])
                .forEach(x => {if (!pack.has(x) && !app.has(x)) app.add(x)})
        }
        app.forEach(x => pack.add(x))
        if (app.size === 0) break
    }
    return new Set(Array.from(pack).filter(x => !isNTS(x)))
}

function FIRST(G: Grammar, beta: string, a: string) {
    const s: StrSet = new Set
    if (beta === '') {
        s.add(a)
    // console.info("[0] FIRST SET of :", beta + a, s)
        return s
    }
    if (!isNTS(beta))
        s.add(beta)
    else
        first(G, beta).forEach(x => s.add(x))
    // const r = first(G, beta)
    // console.info("[1] FIRST SET of :", beta + a, s)
    return s
}

function closure(G: Grammar, I: ItemSet) {
    for (;;) {
        const Iapp: ItemSet = new Set
        for (const item of I) {
            const i_dot = item.prod.indexOf('·')
            if (i_dot === item.prod.length - 1 || !isNTS(item.prod[i_dot + 1])) continue
            const B = item.prod[i_dot + 1]
            for (const prod of G.filter(_prod => _prod[0] === B)) {
                const beta = i_dot + 1 === item.prod.length - 1 ? '' : item.prod[i_dot + 2]
                for (const b of FIRST(G, beta, item.a)) {
                    const next = {prod: [B, '·', ...prod.slice(1)], a: b}
                    if (!itemSetContains(I, next) && !itemSetContains(Iapp, next))
                        Iapp.add(next)
                }
            }
        }
        Iapp.forEach(x => I.add(x))
        if (Iapp.size === 0) break
    }
    return I
}

function goto(G: Grammar, I: ItemSet, X: string) {
    const J: ItemSet = new Set
    for (const item of I) {
        const i_dot = item.prod.indexOf('·')
        if (i_dot === item.prod.length - 1 || item.prod[i_dot + 1] !== X) continue
        J.add({prod: [...(item.prod.slice(0, i_dot)), X, '·', ...(item.prod.slice(i_dot + 2))], a: item.a})
    }
    return closure(G, J)
}

function items(G: Grammar, table: Map<string, string>) {
    // terminals and non-terminals
    const NTS: Set<string> = new Set, TS: Set<string> = new Set;
    classify(G, NTS, TS)
    console.info("terminals :", TS)
    console.info("non-terminals :", NTS)
    // automata, LR(1) item set
    const C: Family = new Set
    C.add(closure(G, SetOf({prod: [G[0][0], '·', G[0][1]], a: '$'})))
    // all grammar symbols
    const GS = new Set([...Array.from(NTS), ...Array.from(TS)])
    for (;;) {
        const Capp: Family = new Set
        let __i = 0
        for (const I of C) {
            for (const X of GS) {
                const J = goto(G, I, X)
                if (J.size !== 0 && !familyContains(C, J) && !familyContains(Capp, J)) {
                // console.info("-- goto from", I, " to", J)
                    Capp.add(J)
                }
                const __j = subscript(C, Capp, J)
                if (__j > -1) {
                    if (!isNTS(X))
                        table.set(`action[${__i}, ${X}]`, `s${__j}`)
                    else
                        table.set(`goto[${__i}, ${X}]`, `${__j}`)
                }
            }
            __i++
        }
        Capp.forEach(x => C.add(x))
        if (Capp.size === 0) break
    }
    // mark reduce items
    {
        let __i = -1
        for (const itemSet of C) {
            __i++
            for (let item of itemSet) {
                const i_dot = item.prod.indexOf('·')
                if (i_dot === item.prod.length - 1 && item.prod[0] !== G[0][0]) {
                    table.set(`action[${__i}, ${item.a}]`, `r${snOfProduction(G, item.prod.filter(s => s !== '·'))}`)
                }
            }
            if (itemSetContains(itemSet, {prod: [...G[0].slice(0, 2), '·'], a: '$'})) {
                table.set(`action[${__i}, $]`, 'acc')
            }
        }
    }
    return C
}

function printGrammar(G: Grammar) {
    console.info("Grammar")
    G.forEach(prod => {
        console.info(`  ${strProduction(prod)}`)
    })
    console.info()
}

function printItems(G: Grammar) {
    console.info("items")
    const table: Map<string, string> = new Map;
    const lr0automata = items(G, table)
    Array.from(lr0automata).forEach((itemSet, i) => {
        console.info(`${i}:`)
        Array.from(itemSet).forEach(item => {
            console.info(`    ${strItem(item)}`)
        })
        console.info()
    })
    console.info("table:")
    table.forEach((v, k, i) => {
        console.info(k, " : ", v)
    })
}

function printClosure(G: Grammar) {
    {
        const s0 = SetOf({prod: ['G', '·', 'S'], a: '$'})
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
}


function printParsingTable(G) {
    const nts: Set<string> = new Set, ts: Set<string> = new Set
    classify(G, nts, ts);
    const table: Map<string, string> = new Map;
    const lr0automata = items(G, table)
    let header = ' '
    const arr_ts  = Array.from(ts)
    const arr_nts = Array.from(nts)
    arr_ts .forEach(s => header += "\t" + s)
    arr_nts.forEach(s => header += "\t" + s)
    console.info(header)
    for (let i = 0; i < lr0automata.size; i++) {
        let line = `${i}`
        for (let j = 0; j < ts.size; j++) {
            const action = `action[${i}, ${arr_ts[j]}]`
            line += "\t"
            if (table.has(action)) {
                line += table.get(action)
            } else {
                line += " "
            }
        }
        for (let j = 0; j < nts.size; j++) {
            const goto = `goto[${i}, ${arr_nts[j]}]`
            line += "\t"
            if (table.has(goto)) {
                line += table.get(goto)
            } else {
                line += " "
            }
        }
        console.info(line)
    }
}

interface StackItem {
    i: number;
    a: string;
}



function printProcess(G: Grammar, table: Map<string, string>, input: string[]) {
    // const input = ['a', 'b', 'b', 'a', 'a', 'b']
    const stack: StackItem[] = [{i: 0, a: '$'}]
    const isShift = (cell) => /^s[1-9][0-9]*$/.test(cell)
    const getShiftState = cell => parseInt(cell.substring(1))
    const getReduceProduction = cell => G[(parseInt(cell.substring(1)))]
    const isReduce = (cell) => /^r[1-9][0-9]*$/.test(cell)
    let nextInputIndex = 0
    const getNextInput = () => (nextInputIndex) < input.length ? input[nextInputIndex] : '$'
    while (1) {
        const a = getNextInput()
        const top = stack[stack.length - 1]
        const action = `action[${top.i}, ${a}]`
        if (!table.has(action)) { console.error(`${action} not in table ${input.length} ${nextInputIndex}`); break; }
        const decision = table.get(action)
        if (decision === 'acc') { break; }
        if (isShift(decision)) {
            stack.push({i: getShiftState(decision), a})
            ++nextInputIndex
        } else if (isReduce(decision)) {
            const prod = getReduceProduction(decision)
            stack.splice(stack.length - prod.length + 1)
            const __top = stack[stack.length - 1]
            const goto = `goto[${__top.i}, ${prod[0]}]`
            stack.push({i: parseInt(table.get(goto)), a: prod[0]})
        }
        console.info(stack)
    }
}





















const G0 = [['G', 'S'], ['S', 'B', 'B'], ['B', 'b', 'B'], ['B', 'a']]
const G1 = [['S', 'E'], ['E', 'E', '+', 'T'], ['E', 'T'], ['T', 'T', '*', 'F'], ['T', 'F'], ['F', '(', 'E', ')'], ['F', 'id']]

function print(G) {



    // console.info("First set of E :", first(G, 'E'))
    // console.info("First set of E :", first(G, 'T'))
    // console.info("First set of E :", first(G, 'F'))

    // printGrammar(G)
    // printClosure(G)
    // printItems(G)
    // printParsingTable(G)
    const table: Map<string, string> = new Map;
    items(G, table)
    console.info("\n\n")
    const inputstream = ['id', '+', 'id', '*', 'id', '+', 'id']
    // const inputstream = ['a', 'b', 'a']
    printProcess(G, table, inputstream)
}

print(G1)


