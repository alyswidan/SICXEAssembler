package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.LineParser;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DirectiveNode extends ParseNode {


    public DirectiveNode(String token) {
    }

    @Override
    public ParseNode nextNode(String token) throws AssemblerException {

        DirectiveResolver resolver = DirectiveResolver.getInstance();
        ParseNode next;

        if (LineParser.getInstance().getMode().equals(LineParser.Mode.SHALLOW))
            next = new SingleArgNode();

        else
            next = new DirectiveArgNode();


        System.out.println(next);
        next.addState("directive", getState("directive"));
        next.addState("arg", token);
        return next;
    }
}
