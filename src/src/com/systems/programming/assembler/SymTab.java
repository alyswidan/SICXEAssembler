package com.systems.programming.assembler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 3/27/2017.
 */
public class SymTab {

    //made the address in the table map string to make it hexadecimal
    private Map<String, String> table = new HashMap<>();
    private static SymTab ourInstance = new SymTab();

    public static SymTab getInstance() {
        return ourInstance;
    }

    private SymTab() {
    }

    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return table.containsValue(value);
    }

    public String get(Object key) {
        return table.get(key);
    }

    public String put(String key, String value) {
        return table.put(key, value);
    }

    public String remove(Object key) {
        return table.remove(key);
    }

    public Map getTable() {
        return table;
    }
}