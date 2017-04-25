package com.systems.programming.assembler;

import java.util.Arrays;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class ObjectCode {

    private int length, flags, arg1, arg2;
    private String opcode;
    private boolean isDirective = false;
    public ObjectCode(String opcode) {

        System.out.println("setting the opcode to "+opcode);
        this.opcode = opcode;
        this.length = -1;
        this.flags = -1;
        this.arg1 = 0;
        this.arg2 = 0;
    }

    public ObjectCode(){}

    public boolean isFormat4(){return (flags&1)==1;}
    public void setOpcode(String opcode) {
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

    public void setLength(int length){
        this.length = length;
    }

    public String getOpcode() {
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

    public int getLength(){
        return length;
    }

    public void setDirective(boolean directive) {
        isDirective = directive;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        System.out.println("flags = " + flags);
        if(isDirective || length == 1)/*directive or format 1*/ {
            System.out.println("format 1 or directive");
            System.out.println(length);
            builder.append(opcode);
        } else if(length==2)//format 2
        {
            System.out.println("format 2");
            builder.append(Integer.toHexString(Integer.parseInt(opcode))).append(String.format("%01x", arg1)).append(String.format("%01x", arg2));
        } else
        {
            int code = Integer.parseInt(getOpcode());
            code <<=4;
            code |= flags;
            if(length==3)
            {
                System.out.println("format 3");
                System.out.println(Integer.toBinaryString(code));
                code<<=12;
                arg1 &=0xfff;
                code|=arg1;
                builder.append(String.format("%06x",code));
            }
            else
            {
                System.out.println("format 4");
                arg1&=0xffffff;
                code<<=20;
                code|=arg1;
                builder.append(String.format("%08x",code));
            }
        }
        System.out.println("hhaha == "+builder.toString());
        return builder.toString();
    }
}
