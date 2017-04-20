package com.systems.programming.assembler;

import java.util.Arrays;

/**
 * Created by Mohamed Mahmoud on 4/13/2017.
 */
public class ObjectCode {

    private int length, flags, arg1, arg2;
    private String opcode;

    public ObjectCode(String opcode) {

        System.out.println("setting the opcode to "+opcode);
        this.opcode = opcode;
        this.length = opcode.length();
        this.flags = 0;
        this.arg1 = 0;
        this.arg2 = 0;
    }

    public ObjectCode(){}

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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if(flags==0 && arg1==0 && arg2 == 0)/*directive or format 1*/
            builder.append(opcode);
        else if(flags==0)//format 2
            builder.append(opcode).append(String.format("%02x",arg1)).append(String.format("%02x",arg1));
        else
        {
            int code = Integer.parseInt(opcode,16);
            code <<=4;
            code |= flags;
            if(length==3)
            {
                code<<=12;
                code|=arg1;
                builder.append(String.format("%06x",code));
            }
            else
            {
                code<<=20;
                code|=arg1;
                builder.append(String.format("%08x",code));
            }
        }
        return builder.toString();
    }
}
