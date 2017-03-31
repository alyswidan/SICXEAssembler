package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

/**
 * Created by ADMIN on 3/29/2017.
 */
/*
        *                            The parser passes the line to the tree and it transitions through correct states
        *                        <- : indicates the input to the node
        *                       RootNode <- first item on line the root node classifies it and sends to one of children
        *                       /    |   \
        *                     /      |   \
        *                    /       |    \
        *           Instruction      |       Label<- second item: if(instruction go to instruction if directive go to directive
        *                            |                   else throw exception)
        *             /  \      Directive<-operands: if valid go to the corresponding node to preserve state else error
        *            /    \         /
        *          /       \       /
        * DoubleArg          SingleArg
        *      \                 /
        *       \              /
        *        \           /
        *         \         /
        *          \       /
        *           \     /
        *            \   /
        *     TerminalNode(its parent is a leaf)
        *
        *
        * */
public abstract class ParseNode {

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        //System.out.println("arrived at node with "+state+" type = "+this.getClass().getSimpleName());

        this.state = state;
    }

    //leaf nodes return null as their next hence optional
    public abstract ParseNode nextNode(String token) throws UnExpectedTokenException;
}
