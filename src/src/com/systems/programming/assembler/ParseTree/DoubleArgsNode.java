package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnknownRegisterException;
import com.systems.programming.assembler.SavedRegisters;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DoubleArgsNode extends ParseNode {

    @Override
    public void addState(String key, String val) throws AssemblerException {
        SavedRegisters savedRegisters = SavedRegisters.getInstance();
        Predicate<String> isReg = savedRegisters::containsValue;
        if (key.equals("arg")) {
            String regs[] = val.split(",");
            if (isReg.test(regs[0])) {
                super.addState("arg1", String.valueOf(savedRegisters.getValue(regs[0])));
                super.addState("arg2", "0");
            } else throw new UnknownRegisterException();

            if (regs.length == 2) {
               // System.out.println(getState("instruction"));
                if (getState("instruction").equalsIgnoreCase("SHIFTL")||
                        getState("instruction").equalsIgnoreCase("SHIFTR")    ) {

                    super.addState("arg2", String.valueOf(Integer.parseInt(regs[1])));
                }
                else if (isReg.test(regs[1])) {
                    //System.out.println(getState("instruction"));
                    super.addState("arg2", String.valueOf(savedRegisters.getValue(regs[1])));
                } else throw new UnknownRegisterException();
            }

        } else
            super.addState(key, val);
    }

    @Override
    public ParseNode nextNode(String token) throws AssemblerException {
        return new TerminalNode();
    }
}
