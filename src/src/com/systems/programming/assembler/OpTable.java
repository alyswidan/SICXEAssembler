package com.systems.programming.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 3/27/2017.
 */
public class OpTable {
    private static OpTable ourInstance = new OpTable();

    //mnemonic->{opcode,format}
    private Map<String, Entry> table = new HashMap<>();

    private OpTable() {
    }

    public static OpTable getInstance() {
        return ourInstance;
    }

    public Map<String, Entry> getTable() {
        return table;
    }

    public boolean isInstruction(String key) {
        if (key.charAt(0) == '+')
            key = key.substring(1);
        return table.containsKey(key.toUpperCase());
    }

    public int getOpcode(String mnemonic) {
        return table.get(mnemonic).opcode;
    }

    //this returns the instruction format if it is a valid instruction else it returns -1
    public int getFormat(String mnemonic) {
        if (mnemonic.charAt(0) == '+') return 4;
        if (table.containsKey(mnemonic))
            return table.get(mnemonic).format;
        else return -1;
    }

    public int getOperandCount(String mnemonic) {
        if (mnemonic.charAt(0) == '+') mnemonic = mnemonic.substring(1);
        return table.get(mnemonic).operandCount;
    }

    //this also returns -1 if the mnemonic isn't a valid instruction
    public int getLength(String mnemonic) {
        return getFormat(mnemonic);
    }

    public void init(File IS) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(IS))) {
            while ((line = br.readLine()) != null) {
                //split on any number of whitespaces more than 2(this avoids spliting mnemonic from operand since some don't have operands)
                String parts[] = line.split("\\s{2,100}");
                /*
                * seperate mnemonic from operand
                * parse format(we only use 1st character of string as to get 3 only out of 3/4)
                * parse opcode as a hex string
                * */
                String[] mnemonicAndOperand = parts[0].split("\\s");
                table.put(parts[0].split("\\s")[0], new Entry(Integer.parseInt(parts[1].substring(0, 1)),
                        Integer.parseInt(parts[2], 16),
                        //if only a mnemonic exists operand count is 0 else split on comma and count resulting tokens
                        mnemonicAndOperand.length == 1 ? 0 : mnemonicAndOperand[1].split("\\,").length));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot read Symbol table from file");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%10s%10s%10s%10s\n", "mnemonic", "opcode", "format", "operands"));
        //append formatted string to builder
        table.keySet().forEach(mnemonic -> builder.append(String.format("%10s%10x%10d%10d\n", mnemonic, getOpcode(mnemonic), getFormat(mnemonic), getOperandCount(mnemonic))));
        return builder.toString();
    }

    private class Entry {
        int opcode, format, operandCount;

        Entry(int format, int opcode, int operandCount) {
            this.opcode = opcode;
            this.format = format;
            this.operandCount = operandCount;
        }
    }

    //used for testing only comment out when not testing this class individually
  /*public static void main(String[] args) {

      OpTable.getInstance().init(new File("E:\\aly\\projects\\SICXE-Assembler\\SICXE-Assembler\\SICXE-BOMBA\\src\\instructionSet"));
      System.out.println(OpTable.getInstance());
      System.out.println(Arrays.toString("C'abcdads".split("'(.*?)'")));
  }*/

}
