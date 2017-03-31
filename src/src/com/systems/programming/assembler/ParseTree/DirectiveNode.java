package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DirectiveNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token)throws UnExpectedTokenException {
        ParseNode node = new SingleArgNode();
        node.setState(token);
        return node;
    }
}
