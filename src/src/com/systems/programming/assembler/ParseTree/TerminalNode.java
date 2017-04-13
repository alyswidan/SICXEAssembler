package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class TerminalNode extends ParseNode {

    @Override
    public ParseNode nextNode(String token) throws UnExpectedTokenException {
        throw new UnExpectedTokenException();
    }
}
