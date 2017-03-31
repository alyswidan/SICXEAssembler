package com.systems.programming.assembler.ParseTree;


import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.OpTable;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class RootNode extends ParseNode {
    @Override
    public ParseNode nextNode(String token)  {
        ParseNode next;
        //an instruction??
        if(OpTable.getInstance().isInstruction(token))next =  new InstructionNode();
        //a directive??
        else if(DirectiveResolver.getInstance().isDirective(token))next =  new DirectiveNode();
        //a label??
        else next =  new LabelNode();
        next.setState(token);
        //wrap it in an optional
        return next;
    }
}
