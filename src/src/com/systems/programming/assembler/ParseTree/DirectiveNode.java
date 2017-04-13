package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DirectiveNode extends ParseNode {

    private String directives[] = {"resb", "resw", "base", "start", "end"};
    private String directive;

    public DirectiveNode(String token) {
        directive = token;
    }

    @Override
    public ParseNode nextNode(String token) throws AssemblerException {
        ParseNode node = new SingleArgNode();
        node.addState(token,null);
        return node;
    }
}
