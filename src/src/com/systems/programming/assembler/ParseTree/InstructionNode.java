package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.LineParser;
import com.systems.programming.assembler.OpTable;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class InstructionNode extends ParseNode {

    public InstructionNode(String token) {
    }

    @Override
    public ParseNode nextNode(String transitionRequest) throws AssemblerException {
        ParseNode next = null;
        UnExpectedTokenException ex = new UnExpectedTokenException();
        int format = OpTable.getInstance().getFormat(getState("instruction"));
        int operandCount = OpTable.getInstance().getOperandCount(getState("instruction"));
        //how many arguments does this instruction need
        switch (operandCount) {
            case 0:
                if (transitionRequest.matches("")) next = new TerminalNode();
                else throw ex;
                break;
            case 1:
                if (transitionRequest.matches("([^,]+(,X)?)")) {

                        if (LineParser.getInstance().getMode() == LineParser.Mode.SHALLOW)
                            next = new SingleArgNode();
                        else if (format == 3 || format == 4)next = getNextState(transitionRequest);
                        else if(format == 2)next =  new DoubleArgsNode();
                } else
                    throw ex;
                break;
            case 2:
                if (transitionRequest.matches(".+,.+(,X)?"))
                    if (LineParser.getInstance().getMode() == LineParser.Mode.SHALLOW)
                        next = new SingleArgNode();
                    else {
                        next = new DoubleArgsNode();
                    }
                else
                    throw ex;
                break;
            default:
                next = null;
        }
        if (next != null) {
            next.addState("instruction",getState("instruction"));
            next.addState("arg", transitionRequest);

        }

        return next;
    }

    private SingleArgNode getNextState(String transitionState) throws AssemblerException {
        SingleArgNode next;
        if (transitionState.matches("#.+")) {
            next = new ImmediateNode();
        } else if (transitionState.matches("@.+")) {
            next = new IndirectNode();
        } else {
            next = new DirectNode();
        }

        next.addState("format", String.valueOf(OpTable.getInstance().getFormat(getState("instruction"))));
        next.addState("isIndexed", String.valueOf(transitionState.matches(".+,X")));
        return next;
    }

    @Override
    public void addState(String key, String val) throws AssemblerException {
        if (key.equalsIgnoreCase("instruction")) {
            super.addState(key, val);
            //System.out.println(val);
            addState("opcode", String.valueOf(OpTable.getInstance().getOpcode(val)));
        } else {
            super.addState(key, val);
        }
    }
}
