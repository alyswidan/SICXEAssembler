package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class DirectivesResNode extends DirectiveArgNode{


    @Override
    public void addState(String key, String val) throws AssemblerException {
        if(key.equals("dirArg")) {
            addState("dirArg0",null);
        }
        removeState("dirArg");
    }

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }


}
