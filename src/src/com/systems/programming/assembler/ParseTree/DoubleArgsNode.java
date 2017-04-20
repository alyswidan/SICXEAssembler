package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnknownRegisterException;
import com.systems.programming.assembler.SavedRegisters;

import java.util.function.DoubleConsumer;
import java.util.function.Predicate;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DoubleArgsNode extends ParseNode {

    private String reg1, reg2;


    @Override
    public void addState(String key, String val) throws AssemblerException {
        if(key.equals("arg"))
        {
            System.out.println(val);
            addState("arg1",val.split(",")[0]);
            addState("arg2",val.split(",")[1]);
        }
        else super.addState(key,val);
    }

    @Override
    public ParseNode nextNode(String token) throws AssemblerException{
        Predicate<String> isReg = s->SavedRegisters.getInstance().containsValue(s);
        if(isReg.test("arg") && isReg.test("arg2"))
            return new TerminalNode();
        throw new UnknownRegisterException();
    }
}
