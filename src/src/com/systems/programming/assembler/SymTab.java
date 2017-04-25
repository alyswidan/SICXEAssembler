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

    public boolean containsKey(String label) {
        return table.containsKey(label);
    }

    public Integer get(String label) {
        return table.get(label);
    }

    public Integer put(String label, Integer address) throws DuplicateLabelException {

        System.out.println("adding label "+label+" to sym table");
        if (containsKey(label)) throw new DuplicateLabelException();
        return table.put(label, address);
    }

    public Integer remove(String label) {
        return table.remove(label);
    }

    public Map<String, Integer> getTable() {
        return table;
    }


}