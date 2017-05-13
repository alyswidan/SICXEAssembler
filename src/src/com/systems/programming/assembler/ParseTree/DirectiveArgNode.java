package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.LineParser;
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
                int radix = getState("directive").equalsIgnoreCase("start")?16:10;

                Integer.parseInt(val,radix);

                super.addState(key,val);
            }
            catch (NumberFormatException ex)
            {
                super.addState(key, String.valueOf(SymTab.getInstance().get(val)));
            }
            if(!resolver.isAssemblerExecutable(getState("directive")) &&
                            !resolver.isAReserve(getState("directive")))
            {

                super.addState("objectCode", DirectiveResolver
                        .getInstance()
                        .getObjectCode(getState("directive"),val)
                        .toString());
                super.addState("type",DirectiveResolver.getInstance().getExpressionType(val).toString());
            }

        }
        else super.addState(key,val);
    }
}
