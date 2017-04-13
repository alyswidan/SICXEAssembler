package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.OpTable;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class LabelNode extends ParseNode {

    @Override
    public ParseNode nextNode(String transitionRequest) throws AssemblerException {
        ParseNode next;
        //an instruction??
        if (OpTable.getInstance().isInstruction(transitionRequest)) {
            //System.out.println(transitionRequest+" not instruction");
            next = new InstructionNode(transitionRequest);
        }
        //a directive??
        else if (DirectiveResolver.getInstance().isDirective(transitionRequest))
            next = new DirectiveNode(transitionRequest);
            // any thing else throw exception
        else throw new UnExpectedTokenException();
        next.addState("instruction",transitionRequest);
        //wrap it in an optional
        return next;
    }
}
