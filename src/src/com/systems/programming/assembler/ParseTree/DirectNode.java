package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class DirectNode extends SingleArgNode{

    private int flags;
    public DirectNode() {}

    @Override
    public void addState(String key, String val) throws AssemblerException {

        if(key.equals("arg")) {
            super.addState(key,val);
            flags = (1<<5)
                    | (1<<4)
                    | (getState("format").equals("3")?0:1)
                    | ((getState("isIndexed").equals("true")?1:0)<<3);
            addState("arg1",String.valueOf(getDisplacement()));
            addState("flags",String.valueOf(flags));
            removeState("arg");
        }
        else
            super.addState(key,val);

    }

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }


}
