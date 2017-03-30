package com.systems.programming.assembler.ParseTree;
import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.OpTable;

import java.util.Optional;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class LabelNode extends ParseNode {
    @Override
    public ParseNode nextNode(String transitionRequest) throws UnExpectedTokenException {
        ParseNode next;
        //an instruction??
        if(OpTable.getInstance().containsKey(transitionRequest))
        {
            //System.out.println(transitionRequest+" not instruction");
            next =  new InstructionNode();
        }
            //a directive??
        else if(DirectiveResolver.getInstance().isDirective(transitionRequest))next =  new DirectiveNode();
        // any thing else throw exception
        else throw new UnExpectedTokenException();
        next.setState(transitionRequest);
        //wrap it in an optional
        return next;
    }
}
