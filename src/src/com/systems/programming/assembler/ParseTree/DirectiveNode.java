package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

import java.util.Optional;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DirectiveNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token)throws UnExpectedTokenException {
        return new SingleArgNode();
    }
}
