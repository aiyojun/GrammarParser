package com.jpro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LRParser {
    private int reduce(Matcher m) {
        return m.lookingAt() ? m.group(0).length() : 0;
    }

    private Pattern prefixPat = Pattern.compile("^[A-Z]+");

    public int matchPrefix(String raw, String patternStr) {
        char chAcc = 65;
        Map<String, Character> allocator = new HashMap<>(16);
        List<String> patternSeq = new ArrayList<>(16);
        while (patternStr.length() > 0) {
            patternStr = patternStr.trim();
            int reduceLength = reduce(prefixPat.matcher(patternStr));
            if (reduceLength > 0) {
                String el = patternStr.substring(0, reduceLength);
                patternSeq.add(el);
                patternStr = patternStr.substring(reduceLength);
                if (!allocator.containsKey(el)) {
                    if (allocator.size() < 26) {
                        allocator.put(el, chAcc++);
                    } else if (allocator.size() == 26) {
                        chAcc = 97;
                        allocator.put(el, chAcc++);
                    } else {
                        throw new RuntimeException("Allocator limitation!");
                    }
                }
            } else {
                patternSeq.add(patternStr.substring(0, 1));
                patternStr = patternStr.substring(1);
            }
        }
        String rpReg = patternSeq.stream()
                .map(s -> allocator.containsKey(s) ? String.format("%c", allocator.get(s)) : s)
                .reduce((s, i) -> String.format("%s%s", s, i))
                .get();
        // ---
        StringBuilder app = new StringBuilder();
        while (raw.length() > 0) {
            raw = raw.trim();
            int reduceLength = reduce(prefixPat.matcher(raw));
            String pre = raw.substring(0, reduceLength);
            app.append(allocator.get(pre));
            raw = raw.substring(reduceLength);
        }
        String rpRaw = app.toString();

//        System.out.println(patternSeq);
//        System.out.println(allocator);
//        System.out.println(rp);
//        System.out.println(rpRaw);

        Pattern fPat = Pattern.compile(rpReg);
        return reduce(fPat.matcher(rpRaw));
    }

    public int eMatchPrefix(List<Token> lt, String psr) {
        return matchPrefix(lt.stream().map(i -> i.getType().toString())
                .reduce((s, i) -> String.format("%s %s", s, i)).get(), psr);
    }


//    public static String replaceRaw(String raw, Map<String, Character> allocator) {
//        Pattern prefixPat = Pattern.compile("^[A-Z]+");
//
//    }



    public static void main(String[] args) {
//        String pattern = "AB(CB)*D?";
//        String str = "ABCBCBD";
//        System.out.println(str.matches(pattern));

        LRParser lrParser = new LRParser();
        System.out.println(lrParser.matchPrefix("IMPORT ID DOT ID", "IMPORT ID (DOT ID)* [SEMI]?"));
    }
}
