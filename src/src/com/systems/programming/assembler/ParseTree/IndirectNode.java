package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class IndirectNode extends SingleArgNode {

    private int flags;

    public IndirectNode() {}

    @Override
    public int getFlags() {
        if(super.getFlags()==-1)
        {
            super.setFlags((1<<5)
                    | (getState("format").equals("3")?0:1)
                    | ((getState("isIndexed").equals("true")?1:0)<<3));
        }
        return super.getFlags();
    }

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }



}
