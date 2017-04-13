package com.systems.programming.assembler;

import java.util.Arrays;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class ObjectCode {

    private int opcode, flags, arg1, arg2;

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public int getOpcode() {
        return opcode;
    }

    public int getFlags() {
        return flags;
    }

    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(getOpcode(),
                getFlags(),
                getArg1(),
                getArg2()).
                forEach(token -> builder.append(String.format("%15s", token == null ? "" : token)));
        return builder.toString();
    }
}
