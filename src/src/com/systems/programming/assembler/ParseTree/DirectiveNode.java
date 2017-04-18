package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

import java.util.Arrays;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class DirectiveNode extends ParseNode {

    private String directivesRes[] = {"resb", "resw", "base"};
    private String directivesBW[] = {"byte", "word"};
    private String directivesBSE[] = {"base", "org", "equ", "start", "end", "nobase"};
    private String directive;

    public DirectiveNode(String token) {
        directive = token;
    }

    @Override
    public ParseNode nextNode(String token) throws AssemblerException {
        ParseNode next;
        UnExpectedTokenException ex = new UnExpectedTokenException();

        if( Arrays.stream(directivesRes).anyMatch(elem -> elem.equals(token))){
            next = new DirectivesResNode();
            next.addState("Res",null);
        }
        else if (Arrays.stream(directivesBSE).anyMatch(elem -> elem.equals(token))){
            next = new DirectivesBSENode();
            next.addState("BSE",null);
        }
        else if (Arrays.stream(directivesBW).anyMatch(elem -> elem.equals(token))){
            next = new DirectivesBWNode();
            //next.addState("BW",);
        }
        else
            throw ex;
        if (next!=null)
            next.addState("dirArg",token);
        return next;
    }
}
