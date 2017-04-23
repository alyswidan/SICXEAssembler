package com.systems.programming.assembler.ParseTree;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class ImmediateNode extends SingleArgNode{

    public ImmediateNode() {}

    @Override
    public int getFlags() {
        if(super.getFlags()==-1)
        {
            setFlags((1<<4)
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
