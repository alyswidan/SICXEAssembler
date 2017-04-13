package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.OpTable;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class RootNode extends ParseNode {

    @Override
    public ParseNode nextNode(String token) throws AssemblerException {
        ParseNode next;
        //an instruction??
        if (OpTable.getInstance().isInstruction(token))
        {
            next = new InstructionNode(token);
            next.addState("instruction",token);
        }
            //a directive??
        else if (DirectiveResolver.getInstance().isDirective(token))
        {
            next = new DirectiveNode(token);
            next.addState("directive",token);
        }
            //a label??
        else
        {
            next = new LabelNode();
            next.addState("label",token);
        }

        if(getState("base")!=null)
            next.addState("base",getState("base"));


        return next;
    }
}
