package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.OpTable;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class InstructionNode extends ParseNode {

    @Override
    public ParseNode nextNode(String transitionRequest) throws UnExpectedTokenException {
        ParseNode next;
        UnExpectedTokenException ex = new UnExpectedTokenException();
        switch (OpTable.getInstance().getOperandCount(getState()))
        {
            case 0:
                if(transitionRequest.matches(""))next = new TerminalNode();
                else throw ex;
                break;
            case 1:
                if(transitionRequest.matches("([^,]+(,X)?)")) next = new SingleArgNode();
                else
                    throw ex;
                break;
            case 2:
                if(transitionRequest.matches(".+,.+(,X)?")) next = new DoubleArgsNode();
                else
                    throw ex;
                break;
            default:next = null;
        }
        if(next!=null)next.setState(transitionRequest);
        return next;
    }
}
