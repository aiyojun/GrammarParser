package com.jpro;

import java.util.*;

public class Node<T> {
    /* Todo:
     *   1. construct ast structure
     *   2. record extra information, eg: position: start->to->end
     *   3. token type string or node name
     **/
    private final String uid;
    private final T value;
    private String text;
    private Node<T> parent;
    private final List<Node<T>> children = new ArrayList<>(16);
    private final Map<String, Object> props = new HashMap<>(16);

    public void addChild(Node<T> c) { children.add(c); c.setParent(this); }
    public void replaceChild(int i, Node<T> c) { children.set(i, c); }
    public boolean hasChild() { return !children.isEmpty(); }
    public int getChildrenCount() { return children.size(); }
    public void removeChild(int i) { children.remove(i); }
    public Node<T> lastChild() { return children.get(children.size() - 1); }
    public Node<T> getChild(int i) { return children.get(i); }
    public List<Node<T>> getChildren() { return children; }
    public Node<T> getParent() { return parent; }
    public void setParent(Node<T> parent) { this.parent = parent; }
    public void setText(String t) { this.text = t; }
    public String getText() { return text; }
    public void putProp(String key, Object value) { this.props.put(key, value); }
    public Object getProp(String key) { return this.props.get(key); }
    public T getValue() { return value; }
    public String getUid() { return uid; }
    private Node(T v) { uid = UUID.randomUUID().toString().replace("-", ""); this.value = v; }
    public static <T> Node<T> build(T v) { return new Node<>(v); }
}
