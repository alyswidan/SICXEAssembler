package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;

import java.util.Optional;

/**
 * Created by ADMIN on 3/29/2017.
 */
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
