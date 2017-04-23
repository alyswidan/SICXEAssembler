package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.SymTab;
import sun.awt.Symbol;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class DirectiveArgNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }

    @Override
    public void addState(String key, String val) throws AssemblerException {
        DirectiveResolver resolver = DirectiveResolver.getInstance();

        if(key.equalsIgnoreCase("arg") )
        {

            try {
                Integer.parseInt(val);
                super.addState(key,val);
            }
            catch (NumberFormatException ex)
            {
                super.addState(key, String.valueOf(SymTab.getInstance().get(val)));
            }
            if(!resolver.isAssemblerExecutable(getState("directive")) &&
                            !resolver.isAReserve(getState("directive")))
            {
                System.out.println("val = " + val);
                System.out.println(getState("directive"));
                super.addState("objectCode", DirectiveResolver
                        .getInstance()
                        .getObjectCode(getState("directive"),val)
                        .toString());
            }

        }
        else super.addState(key,val);
    }
}
