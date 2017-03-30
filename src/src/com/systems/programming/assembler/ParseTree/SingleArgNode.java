package com.systems.programming.assembler.ParseTree;


import java.util.Optional;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class SingleArgNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }
}
