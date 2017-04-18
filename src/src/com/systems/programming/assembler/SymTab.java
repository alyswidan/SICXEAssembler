package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.DuplicateLabelException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 3/27/2017.
 */
public class SymTab {

    private static SymTab ourInstance = new SymTab();
    //made the address in the table map string to make it hexadecimal
    private Map<String, Integer> table = new HashMap<>();

    private SymTab() {
    }

    public static SymTab getInstance() {
        return ourInstance;
    }

    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return table.containsValue(value);
    }

    public Integer get(Object key) {
        return table.get(key);
    }

    public Integer put(String key, Integer value) throws DuplicateLabelException {

        if (containsKey(key)) throw new DuplicateLabelException();
        return table.put(key, value);

    }

    public Integer remove(Object key) {
        return table.remove(key);
    }

    public Map<String, Integer> getTable() {
        return table;
    }


}