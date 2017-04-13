package com.systems.programming.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 4/12/2017.
 */
public class SavedRegisters {

    private static SavedRegisters ourInstance = new SavedRegisters();

    //table<name,number>
    private Map<String, String> regTable = new HashMap<>();

    private SavedRegisters() {

    }

    public static SavedRegisters getInstance() {
        return ourInstance;
    }

    public Map<String, String> getRegTable() {
        return regTable;
    }

    public boolean containsValue(Object key) {
        return regTable.containsKey(key);
    }

    public void init(File savedReg) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(savedReg))) {
            while ((line = br.readLine()) != null) {
                //split on any number of whitespaces more than 2
                String parts[] = line.split("\\s{2,100}");
                //put the register name and number
                regTable.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot read Symbol table from file");
        }
    }
}
