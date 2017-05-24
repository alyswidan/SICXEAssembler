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
        //System.out.println("transitionRequest = " + transitionRequest);
        ParseNode next;
        //an instruction??
        if (OpTable.getInstance().isInstruction(transitionRequest)) {
            //System.out.println(transitionRequest +" is an instruction");
            next = new InstructionNode(transitionRequest);
            next.addState("instruction",transitionRequest);
        }

        //a directive??
        else if (DirectiveResolver.getInstance().isDirective(transitionRequest)) {
            //System.out.println(transitionRequest +" is a directive");
            next = new DirectiveNode(transitionRequest);
            next.addState("directive",transitionRequest);
        }// any thing else throw exception

        else
        {
            System.out.println(transitionRequest +" is unexpected");
            throw new UnExpectedTokenException();
        }
        return next;
    }
}
