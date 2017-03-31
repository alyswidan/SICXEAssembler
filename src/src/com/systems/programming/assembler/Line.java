package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.UndefinedMnemonicException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ADMIN on 3/30/2017.
 */
public class Line {
    private String label, mnemonic, operand, comment,line;

    public Line(String line) {
        this.line = line;
    }
    public boolean isEmpty()
    {
        return line.matches("^(\\n*|\\s*)$");
    }
    //checks if this line is a comment :D
    public boolean isComment() {
        //this matches any any numbe of white spaces at beginning then a dot and any comment
        return line.matches("\\s*\\..*");
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMnemonic() {return mnemonic;}
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.replaceAll("\\s","");
    }

    public boolean isStart(){return mnemonic.contains("start");}
    //classifies mnemonic and gets length of instruction
    public int getLength() throws UndefinedMnemonicException {
        int len=-1;
        if(OpTable.getInstance().isInstruction(getMnemonic()))
            len = OpTable.getInstance().getLength(getMnemonic());
        else if(DirectiveResolver.getInstance().isDirective(getMnemonic()))
            len = DirectiveResolver.getInstance().getLength(getMnemonic(), getOperand());
        if (len == -1) throw new UndefinedMnemonicException();
        return len;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(getLabel(),
                        getMnemonic(),
                        getOperand(),
                        getComment()).
                        forEach(token-> builder.append(String.format("%15s",token==null?"":token)));
        return builder.toString();
    }
}
