package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.DuplicateLabelException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 3/27/2017.
 */
public class SymTab {

    public enum Type {RELATIVE,ABSOLUTE};
    private static SymTab ourInstance = new SymTab();
    //made the address in the table map string to make it hexadecimal
    private Map<String, Attributes> table = new HashMap<>();

    private SymTab() {}

    public static SymTab getInstance() {
        return ourInstance;
    }

    public boolean containsKey(String label) {
        return table.containsKey(label);
    }

    public Integer get(String label) {
        System.out.println("get label = " + label);
        return table.get(label).getVal();
    }

    public Type getType(String label){
        return table.get(label).getType();
    }
    public String getCSect(String label)
    {
        return table.get(label).getcSect();
    }

    public Integer put(String label, Integer address) throws DuplicateLabelException {
        if (containsKey(label)) throw new DuplicateLabelException();
        Attributes attr = new Attributes(address);
        attr.setType(Type.ABSOLUTE);
        attr.setcSect(Assembler.getProgName());
        table.put(label,attr);
        return address;
    }
    //adds whole entery to table
    public void putFull(String label,int address,Type type,String cSect)throws DuplicateLabelException
    {
        if (containsKey(label)) throw new DuplicateLabelException();
        table.put(label,new Attributes(address,type,cSect));
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //write the symbolTable
        builder.append("===================================================\n");
        builder.append(String.format("|%10s%5c%10s%5c%10s%5c%10s|\n", "Symbol", '|', "address", '|',"Type",'|',"CSect"));
        builder.append("====================================================\n");
        table.forEach((k, v) -> builder.append(String.format("|%10s%5c%10X%5c%10s%5c%10s|\n",
                            k, '|', v.getVal(), '|',v.getType(),'|',v.getcSect())));
        builder.append("====================================");
        return builder.toString();
    }

    private static class Attributes
    {
        private int val;
        private String cSect;
        private Type type;

        public Attributes(int val, Type type,String cSect) {
            this.val = val;
            this.cSect = cSect;
            this.type = type;
        }

        public Attributes(int val) {
            this.val = val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public void setcSect(String cSect) {
            this.cSect = cSect;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public int getVal() {
            return val;
        }

        public String getcSect() {
            return cSect;
        }

        public Type getType() {
            return type;
        }
    }
}