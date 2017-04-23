package com.systems.programming.assembler.Exceptions;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class DisplacementOutOfBoundException extends AssemblerException {

    String who;

    public DisplacementOutOfBoundException(String who) {
        this.who = who;
    }

    public DisplacementOutOfBoundException() {
    }

    @Override
    public String getMessage() {
        return who;
    }
}
