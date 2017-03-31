package com.systems.programming.assembler.ParseTree;


/**
 * Created by ADMIN on 3/29/2017.
 */
public class DoubleArgsNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }
}
