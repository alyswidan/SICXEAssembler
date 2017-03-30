package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.OpTable;


import java.util.Optional;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class InstructionNode extends ParseNode {

    @Override
    public ParseNode nextNode(String transitionRequest) throws UnExpectedTokenException {
        ParseNode next;

        switch (OpTable.getInstance().getOperandCount(getState()))
        {
            case 0:
                if(transitionRequest.matches(""))next = new TerminalNode();
                else throw new UnExpectedTokenException();
                break;
            case 1:
                if(transitionRequest.matches("([^,]+(,X)?)")) next = new SingleArgNode();
                else
                    throw new UnExpectedTokenException();
                break;
            case 2:
                if(transitionRequest.matches(".+,.+(,X)?")) next = new DoubleArgsNode();
                else
                    throw new UnExpectedTokenException();
                break;
            default:next = null;
        }
        if(next!=null)next.setState(transitionRequest);
        return next;
    }
}
