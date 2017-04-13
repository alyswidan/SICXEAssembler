package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String,String> state;

    public Map<String,String> getState() {
        return state;
    }

    public ParseNode() {
        state = new HashMap<>(7);
    }
    public ParseNode(Map<String, String> state) {
        this.state = state;
    }

    public void setState(Map<String,String> state) {
        //System.out.println("arrived at node with "+state+" type = "+this.getClass().getSimpleName());
        this.state = state;
    }

    public void addState (String key,String val) throws AssemblerException
    {
        state.put(key,val);
    }

    public String removeState(String key) {
        return state.remove(key);
    }

    public String getState(String key)
    {
        return state.get(key);

    }

    //leaf nodes return null as their next hence optional
    public abstract ParseNode nextNode(String token) throws AssemblerException;
}
