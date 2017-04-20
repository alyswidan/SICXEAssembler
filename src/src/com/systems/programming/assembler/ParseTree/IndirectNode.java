package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class IndirectNode extends SingleArgNode {

    private int flags;

    public IndirectNode() {}

    @Override
    public void addState(String key, String val) throws AssemblerException {
        if(key.equals("arg")) {
            super.addState(key,val);
            flags = (1<<5)
                    | (getState("format").equals("3")?0:1)
                    | ((getState("isIndexed").equals("true")?1:0)<<3);
            addState("arg",val.replace("@", ""));
            addState("arg1",String.valueOf(getDisplacement()));
            addState("flags",String.valueOf(flags));
            removeState("arg");
        }
        super.addState(key,val);
    }

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }



}
