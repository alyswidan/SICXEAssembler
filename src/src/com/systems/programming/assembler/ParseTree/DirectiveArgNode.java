package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class DirectiveArgNode extends ParseNode {

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }
}
