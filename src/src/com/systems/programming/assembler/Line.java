package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.UndefinedMnemonicException;

import java.util.Arrays;

/**
 * Created by ADMIN on 3/30/2017.
 */
public class Line {
    private static String start, pLength;
    private String label, mnemonic, operand, comment, line, objcode;

    public Line(String line) {
        this.line = line;
    }

    public static String getStart() {
        return start;
    }

    public static String getpLength() {
        return pLength;
    }

    public boolean isEmpty() {
        return line.matches("^(\\n*|\\s*)$");
    }

    //checks if this line is a comment :D
    public boolean isComment() {
        //this matches any any number of white spaces at beginning then a dot and any comment
        return line.matches("\\s*\\..*");
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

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
        this.comment = comment.replaceAll("\\s", "");
    }

    public String getObjcode() {
        return objcode;
    }

    public void setObjcode(String objcode) {
        this.objcode = objcode;
    }

    public boolean isStart() {
        if (mnemonic.equalsIgnoreCase("start"))
            start = this.getOperand();
        return mnemonic.equalsIgnoreCase("start");
    }

    public boolean isEnd() {
        if (mnemonic.equalsIgnoreCase("end"))
            setLength(Integer.toString(Integer.parseInt(this.getOperand(), 16) - Integer.parseInt(start, 16)));
        return mnemonic.equalsIgnoreCase("end");
    }

    //classifies mnemonic and gets length of instruction
    public int getLength() throws UndefinedMnemonicException {
        int len = -1;
        if (OpTable.getInstance().isInstruction(getMnemonic()))
            len = OpTable.getInstance().getLength(getMnemonic());
        else if (DirectiveResolver.getInstance().isDirective(getMnemonic()))
            len = DirectiveResolver.getInstance().getLength(getMnemonic(), getOperand());
        if (len == -1) throw new UndefinedMnemonicException();
        return len;
    }

    public static void setLength(String pLength) {
        Line.pLength = pLength;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(getLabel(),
                getMnemonic(),
                getOperand(),
                getComment()).
                forEach(token -> builder.append(String.format("%15s", token == null ? "" : token)));
        return builder.toString();
    }
}
