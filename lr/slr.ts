// collection of LR(0) item set
type Item           = Array<string>;
type Production     = Item;
type ItemSet        = Set<Item>;
type Family         = Set<ItemSet>;
type Grammar        = Array<Production>;

function isNTS(s: string) { return /[A-Z]/.test(s) }

function strSet(set: Set<string>) { return "{ " + Array.from(set).join(", ") + " }" }

function strItem(item: Item) { return item[0] + " -> " + item.slice(1).join(' ') }

function strItemSet(closure: ItemSet) { return Array.from(closure).map(s => strItem(s)).join('\n') }

function setContains(_item: Item, _set: ItemSet) {
    const mItem = strItem(_item)
    return Array.from(_set).map(i => strItem(i)).includes(mItem)
}

function classify(grammar: Grammar, nts: Set<string>, ts: Set<string>) {
    for (let i = 0; i < grammar.length; i++) {
        const prod = grammar[i]
        nts.add(prod[0])
        prod.slice(1).forEach(s => {
            if (isNTS(s))
                nts.add(s)
            else
                ts.add(s)
        })
    }
    return nts
}

function isOneState(state0: ItemSet, state1: ItemSet) {
    if (state0.size !== state1.size) return false
    for (const item of state0) {
        if (!setContains(item, state1))
            return false
    }
    return true
}

function familyContain(family: Family, itemSet: ItemSet) {
    for (const member of family) {
        if (isOneState(member, itemSet))
            return true
    }
    return false
}

function NewItemSet(...args) {
    const set: ItemSet = new Set
    args.forEach(arg => set.add(arg))
    return set
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

function closure(G: Grammar, I: ItemSet) {
    for (;;) {
        const I_: ItemSet = new Set
        for (const item of I) {
            const i = item.indexOf('·')
            if (i === item.length - 1 || !isNTS(item[i + 1])) continue
            for (const prd of G.filter(prod => prod[0] === item[i + 1])) {
                const c: Item = [prd[0], '·', ...prd.slice(1)]
                if (!setContains(c, I) && !setContains(c, I_))
                    I_.add(c)
            }
        }
        I_.forEach(x => I.add(x))
        if (I_.size === 0) break
    }
    return I
}

function goto(I: ItemSet, X: string) {
    const C: ItemSet = new Set
    for (const item of I) {
        const i = item.indexOf('·')
        if (i === item.length - 1 || item[i + 1] !== X)
            continue
        C.add([...(item.slice(0, i)), item[i + 1], '·', ...(item.slice(i + 2))])
    }
    return C
}

function items(G: Grammar) {
    const C: Family = new Set
    const NTS: Set<string> = new Set, TS: Set<string> = new Set;
    classify(G, NTS, TS)
    const S = new Set([...Array.from(NTS), ...Array.from(TS)])
    C.add(closure(G, NewItemSet([G[0][0], '·', G[0][1]])))
    for (;;) {
        let C_: Family = new Set;
        for (const I of C) {
            for (const X of S) {
                const nextState = closure(G, goto(I, X))
                if (nextState.size !== 0 && (!familyContain(C, nextState) && !familyContain(C_, nextState))) {
                    C_.add(nextState)
                }
            }
        }
        C_.forEach(each => C.add(each))
        if (C_.size === 0)
            break
    }
    return C
}




const G0 = [['S', 'E'], ['E', 'E', '+', 'T'], ['E', 'T'], ['T', 'T', '*', 'F'], ['T', 'F'], ['F', '(', 'E', ')'], ['F', 'id']]
const G1 = [['G', 'S'], ['S', 'B', 'B'], ['B', 'b', 'B'], ['B', 'a']]



function test_closure() {
    const G = [['S', 'E'], ['E', 'E', '+', 'T'], ['E', 'T'], ['T', 'T', '*', 'F'], ['T', 'F'], ['F', '(', 'E', ')'], ['F', 'id']]
    console.info("Grammar : ", G.map(a => strItem(a)))
    // const nts: Set<string> = new Set; const ts: Set<string> = new Set;
    // classify(G, nts, ts)
    // console.info("Non terminal : ", Array.from(nts))
    // console.info("    Terminal : ", Array.from(ts))
    {
        const s0 = NewItemSet(['S', '·', 'E'])
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
    {
        const s0 = NewItemSet(['E', 'T', '·'])
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
    {
        const s0 = NewItemSet(['F', '(', '·', 'E', ')'])
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
    {
        const s0 = NewItemSet(['E', 'E', '+', '·', 'T'])
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
    {
        const s0 = NewItemSet(['E', 'E', '+', 'T', '·'])
        console.info("Closure[", Array.from(s0).map(s => strItem(s)), "] is ", Array.from(closure(G, s0)).map(arr => strItem(arr)))
    }
}

function test_goto() {
    /*
      "S -> · E",
      "E -> · E + F",
      "E -> · F",
      "F -> · F * T",
      "F -> · T",
      "T -> · ( E )",
      "T -> · id"
    */
    const itemSet: ItemSet = new Set
    itemSet.add(['S', '·', 'E'])
    itemSet.add(['E', '·', 'E', '+', 'F'])
    itemSet.add(['E', '·', 'F'])
    itemSet.add(['F', '·', 'F', '*', 'T'])
    itemSet.add(['F', '·', 'T'])
    itemSet.add(['T', '·', '(', 'E', ')'])
    itemSet.add(['T', '·', 'id'])
    console.info("---")
    console.info("test_goto() : ", Array.from(itemSet).map(s => strItem(s)))
    console.info("GOTO(I, E) : ", Array.from(goto(itemSet, 'E')).map(s => strItem(s)))
    console.info("GOTO(I, F) : ", Array.from(goto(itemSet, 'F')).map(s => strItem(s)))
    console.info("GOTO(I, '(') : ", Array.from(goto(itemSet, '(')).map(s => strItem(s)))
    console.info("GOTO(I, 'id') : ", Array.from(goto(itemSet, 'id')).map(s => strItem(s)))
    console.info("---")
}

function printItems(G: Grammar) {
    console.info("items")
    const lr0automata = items(G)
    Array.from(lr0automata).forEach((itemSet, i) => {
        console.info(`${i}:`)
        Array.from(itemSet).forEach(item => {
            console.info(`    ${strItem(item)}`)
        })
        console.info()
    })
}

function printGrammar(G: Grammar) {
    console.info("Grammar")
    G.forEach(prod => {
        console.info(`  ${strItem(prod)}`)
    })
    console.info()
}

function printFirstSet(G: Grammar) {
    G.reduce((prev, prod) => { prev.add(prod[0]); return prev; }, new Set<string>())
        .forEach(x => console.info(`FIRST(${x}) =`, strSet(first(G, x))))
    console.info()
}

// test_closure()

// test_goto()
function print(G0) {
    printGrammar(G0)
    printFirstSet(G0)
    printItems(G0)
}

print(G0)




