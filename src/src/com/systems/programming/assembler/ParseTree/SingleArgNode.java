package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.DisplacementOutOfBoundException;
import com.systems.programming.assembler.LineParser;
import com.systems.programming.assembler.SymTab;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class SingleArgNode extends ParseNode {

    private int flags;

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }

    public int getDisplacement() throws AssemblerException
    {
        int disp;
        try {
            disp = Integer.parseInt(getState("arg"));
        }catch (NumberFormatException ex)
        {
            if(!SymTab.getInstance().containsKey(getState("arg")))
                throw new DisplacementOutOfBoundException();
            disp = computeRelativeDisp(SymTab.getInstance().get(getState("arg")));
        }
        return disp;
    }

    public int computeRelativeDisp(int arg) throws DisplacementOutOfBoundException
    {
        LineParser parser = LineParser.getInstance();
        //pc relative?
        if(arg>-2047 && arg<2048)
        {
            flags |= 1<<1;
            return arg - (parser.getCurrentLocation()+Integer.parseInt(getState("format")));
        }
        //base relative
        else if(parser.isBaseActivated() && arg<4048)
        {
            flags |= 1<<2;
            return arg - parser.getBase();
        }
        //neither
        throw new DisplacementOutOfBoundException();
    }

}
